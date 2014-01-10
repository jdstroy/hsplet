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

package hsplet.compiler.codescanner;

/**
 *
 * @author jdstroy
 */
public class MethodDescriptor {

    public String name, desc, signature;

    public MethodDescriptor(String name, String desc, String signature) {
        this.name = name;
        this.desc = desc;
        this.signature = signature;
    }

    public String getDesc() {
        return desc;
    }

    public String getName() {
        return name;
    }

    public String getSignature() {
        return signature;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setSignature(String signature) {
        this.signature = signature;
    }
    
}
