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

package org.yi.jdstroy.hsplet;

import java.io.Serializable;
import java.nio.file.FileSystem;
import org.yi.jdstroy.hsplet.io.IFileSystemTranslator;
import org.yi.jdstroy.hsplet.io.UriDecodeTranslator;

/**
 *
 * @author jdstroy
 */
public class Configuration implements Serializable {
    
    protected Class<? extends IFileSystemTranslator> fileSystemTranslatorClass;

    public Class<? extends IFileSystemTranslator> getFileSystemTranslatorClass() {
        return fileSystemTranslatorClass;
    }
    
    public static Configuration getDefaultConfiguration() {
        Configuration c = new Configuration();
        c.fileSystemTranslatorClass = UriDecodeTranslator.class;
        return c;
    }
    
}
