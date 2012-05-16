/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.AbstractList;
import java.util.ArrayList;

/**
 *
 * @author jdstroy
 */
public class SparseArrayList<T> extends AbstractList<T> {

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
