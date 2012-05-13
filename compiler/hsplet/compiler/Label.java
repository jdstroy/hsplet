/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.AbstractList;
import java.util.AbstractSet;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.Iterator;
import java.util.NoSuchElementException;
import java.util.Set;

/**
 *
 * @author jdstroy
 */
class Label extends org.objectweb.asm.Label {

    public int branchesToHere = 0;
    public int currentCount = 0;
    public int myIndex = -1;
    public boolean isMainLabel = false;
    public boolean isUsed = false;
    public Object extra;
    public Set<Label> extra_labels;
    public ArrayList<Integer> extra_integers;

    public Set<Label> extra_labels() {
        if (extra_labels == null) {
            extra_labels = new HashSet<Label>();
        }
        return extra_labels;
    }

    public ArrayList<Integer> extra_integers() {
        if (extra_integers == null) {
            extra_integers = new ArrayList<Integer>();
        }
        return extra_integers;
    }

    public void relyOn(Label other) {
        if (isUsed) {
            return;
        }

        if (other == this) {
            return;
        }

        if (other.isUsed) {
            isUsed = true;
            extra = null;
        } else if (extra == null) {
            extra = other;
        } else if (extra instanceof Label) {
            Label old = (Label) extra;
            extra = new HashSet<Label>();
            ((HashSet) extra).add(old);
            ((HashSet) extra).add(other);
        } else if (extra instanceof HashSet) {
            ((HashSet) extra).add(other);
        } else {
            throw new RuntimeException("Error in label logic! Unknown thing in extra in second scan.");
        }
    }
}

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

class SparseArrayList<T> extends AbstractList<T> {

    private T initial;
    public ArrayList<T> list;
    private int size = 0;

    @Override
    public void add(int index, T element) {
        if (size == 0) {
            if (index != 0) {
                throw new IndexOutOfBoundsException();
            }
            initial = element;
        } else {
            if (list == null) {
                list = new ArrayList<T>();
                list.add(initial);
            }
            list.add(element);
        }
        size++;
    }

    @Override
    public T remove(int index) {
        checkIndexAccess(index);
        T retVal;
        if (size == 1) {
            retVal = initial;
            initial = null;
        } else {
            retVal = list.remove(index);
            if (list.size() == 1) {
                initial = list.remove(0);
                list = null;
            }
        }
        size--;
        return retVal;
    }

    @Override
    public T set(int index, T element) {
        checkIndexAccess(index);
        if (size == 1) {
            return initial;
        } else {
            return list.set(index, element);
        }
    }

    private void checkIndexAccess(int index) throws IndexOutOfBoundsException {
        if (size <= index || index < 0) {
            throw new IndexOutOfBoundsException();
        }
    }

    @Override
    public T get(int index) {
        checkIndexAccess(index);
        if (size == 1) {
            return initial;
        } else {
            return list.get(index);
        }
    }

    @Override
    public int size() {
        return size;
    }
}