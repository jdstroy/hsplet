/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.AbstractSet;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 *
 * @author jdstroy
 */
class SparseHashSet<T> extends AbstractSet<T> {

    private HashSet<T> set;
    private T initial;

    @Override
    public boolean add(T e) {
        if (initial == null) {
            initial = e;
            return true;
        } else if (set == null) {
            set = new HashSet<T>();
            set.add(initial);
            return set.add(e);
        } else {
            return set.add(e);
        }
    }

    @Override
    public Iterator<T> iterator() {

        if (initial == null) {
            return new Iterator<T>() {

                @Override
                public boolean hasNext() {
                    return false;
                }

                @Override
                public T next() {
                    throw new NoSuchElementException("No more elements.");
                }

                @Override
                public void remove() {
                    throw new IllegalStateException("No more elements.");
                }
            };
        } else if (set == null) {
            return new Iterator<T>() {

                private boolean nextCalled = false;

                @Override
                public boolean hasNext() {
                    return !nextCalled;
                }

                @Override
                public T next() {
                    if (nextCalled) {
                        throw new NoSuchElementException("No more elements.");
                    }
                    nextCalled = true;
                    return initial;
                }

                @Override
                public void remove() {
                    if (!nextCalled || SparseHashSet.this.initial == null) {
                        throw new IllegalStateException();
                    }
                    SparseHashSet.this.initial = null;
                }
            };
        } else {
            return set.iterator();
        }
    }

    @Override
    public int size() {
        if (initial == null) {
            return 0;
        } else if (set == null) {
            return 1;
        } else {
            return set.size();
        }
    }
}

