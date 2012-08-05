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
package org.yi.jdstroy.commons.winapi;

import java.util.Map;
import java.util.TreeMap;
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jdstroy
 */
public class ProcessContext {

    private AtomicInteger lastHandle = new AtomicInteger();
    private Map<Integer, Object> handleMap = new TreeMap<Integer, Object>();
    
    public <T> int add(T object) {
        int handle = lastHandle.getAndIncrement();
        handleMap.put(handle, object);
        return handle;
    }

    public void remove(int handle) {
        handleMap.remove(new Integer(handle));
    }

    public <T> int find(T object) {
        for (Map.Entry<Integer, Object> entry : handleMap.entrySet()) {
            if (entry.getValue().equals(object)) {
                return entry.getKey();
            }
        }
        return Winbase.INVALID_HANDLE;
    }
}
