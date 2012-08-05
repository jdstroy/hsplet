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
