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

package org.yi.jdstroy.hsplet.io;

import java.io.IOException;

/**
 * A IFileSystemTranslator that converts all inbound paths (from HSP) with 
 * back slashes to forward slashes, and all outbound paths (to HSP) with forward
 * slashes to back slashes.
 * @author jdstroy
 */
public class SlashTranslator implements IFileSystemTranslator {

    @Override
    public String toHSP(String file) throws IOException {
        return file.replace('/', '\\');
    }

    @Override
    public String fromHSP(String fileName) throws IOException {
        return fileName.replace('\\', '/');
    }
    
}
