/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.yi.jdstroy.commons;

import java.awt.EventQueue;
import java.util.concurrent.*;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 *
 * @author jdstroy
 */
public abstract class EDTCallable {

    public static <T> Future<T> submitCallable(Callable<T> callable) {
        EDTFuture<T> edf = new EDTFuture<T>(callable);
        EventQueue.invokeLater(edf);
        return edf;
    }

    private static class EDTFuture<T> implements Runnable, Future<T> {

        private T result;
        private Thread thread;
        private Exception ex;
        private Callable<T> callable;
        private boolean cancelled = false;
        private volatile boolean done = false;
        private boolean firstCancel = true;
        private Lock lock = new ReentrantLock();

        public EDTFuture(Callable<T> c) {
            this.callable = c;
            lock.lock();
        }

        public void run() {
            try {
                thread = Thread.currentThread();
                if (!cancelled) {
                    result = callable.call();
                }
            } catch (Exception ex) {
                this.ex = ex;
            }
            done = true;
            if (!cancelled) {
                lock.unlock();
            }
        }

        @Override
        public boolean cancel(boolean mayInterruptIfRunning) {
            try {
                if (mayInterruptIfRunning && thread != null) {
                    thread.interrupt();
                }
                return (!cancelled) && (!done);
            } finally {
                cancelled = true;
                done = true;
                if (firstCancel) {
                    lock.unlock();
                    firstCancel = false;
                }
            }
        }

        @Override
        public boolean isCancelled() {
            return cancelled;
        }

        @Override
        public boolean isDone() {
            return done;
        }

        @Override
        public T get() throws InterruptedException, ExecutionException {
            if (!done) {
                lock.lockInterruptibly();
                lock.unlock();
            }
            return __get();
        }

        private T __get() throws InterruptedException, ExecutionException {
            if (cancelled) {
                throw new CancellationException();
            }
            if (ex == null) {
                return result;
            }
            throw new ExecutionException(ex);
        }

        @Override
        public T get(long timeout, TimeUnit unit) throws InterruptedException, ExecutionException, TimeoutException {
            if (done) {
                return __get();
            }
            if (lock.tryLock(timeout, unit)) {
                lock.unlock();
                return __get();
            } else {
                throw new TimeoutException();
            }
        }
    }
}