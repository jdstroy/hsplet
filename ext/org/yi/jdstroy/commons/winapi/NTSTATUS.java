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

/**
 *
 * @author jdstroy
 */
public enum NTSTATUS {

    STATUS_SUCCESS(0), 
    STATUS_ACCESS_DENIED(0xc0000022),
    ;
    private int value;

    public int value() {
        return value;
    }

    private NTSTATUS(int value) {
        this.value = value;
    }
}
