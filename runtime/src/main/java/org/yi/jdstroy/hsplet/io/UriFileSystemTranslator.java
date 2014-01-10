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
import java.net.URI;
import java.net.URISyntaxException;

/**
 *
 * @author jdstroy
 */
public class UriFileSystemTranslator {

    public UriFileSystemTranslator(IFileSystemTranslator target) {
        this.target = target;
    }
    private IFileSystemTranslator target;

    public URI fromHSP(String path) throws URISyntaxException, IOException {
        return new URI(target.fromHSP(path));
    }

    public String toHSP(URI path) throws IOException {
        return target.toHSP(path.toString());
    }
}
