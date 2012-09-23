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
import java.lang.reflect.InvocationTargetException;
import java.util.concurrent.Callable;
import java.util.concurrent.Future;
import java.util.concurrent.FutureTask;

/**
 *
 * @author jdstroy
 */
public class EDTUtils {
    
    /**
     * Submits a <code>Callable</code> to the <code>EventQueue</code>
     * @param <T> The return type of the submitted task
     * @param callable A task to run on the EDT
     * @return A <code>Future</code> corresponding to the results of the submitted task.
     * @see java.awt.EventQueue
     */
    public static <T> Future<T> submitCallable(Callable<T> callable) {
        FutureTask<T> edf = new FutureTask<T>(callable);
        EventQueue.invokeLater(edf);
        return edf;
    }
    
    /**
     * Submits a task to the EventQueue to run in the Dispatch Thread; this 
     * method may block if the the calling thread is not the Dispatch Thread.
     * @param runnable 
     */
    
    public static void invokeAndWait(Runnable runnable) throws InvocationTargetException, InterruptedException {
        if (EventQueue.isDispatchThread()) {
            runnable.run();
        } else {
            EventQueue.invokeAndWait(runnable);
        }
    }
}