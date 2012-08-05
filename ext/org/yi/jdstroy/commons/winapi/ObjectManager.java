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
import java.util.concurrent.ConcurrentMap;
import java.util.concurrent.ConcurrentSkipListMap;

/**
 *
 * @author jdstroy
 */
public class ObjectManager {

    private Object dataStructureLock = new Object();
    private ConcurrentMap<String, WinObject<?>> namedObjects = new ConcurrentSkipListMap<String, WinObject<?>>();

    /**
     * Stores an object if its name is not taken in the object manager.
     *
     * @param <T> Type of object
     * @param name The name of the object
     * @param object The object to store in this ObjectManager
     * @param type Runtime type of the object
     * @return null if no object exists at the given name; otherwise, returns
     * the object with the given name if it is assignment-compatible with type
     * @throws ClassCastException if the object with the given name is
     * incompatible with type
     */
    public <T> T putIfAbsent(String name, T object, Class<T> type) {
        WinObject<?> res = namedObjects.putIfAbsent(name, new WinObject<T>(object));
        if (res == null) {
            return null;
        } else {
            return type.cast(res.get());
        }
    }
}
