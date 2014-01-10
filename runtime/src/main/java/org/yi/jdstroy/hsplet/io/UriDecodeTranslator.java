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
import org.apache.commons.codec.DecoderException;

/**
 *
 * @author jdstroy
 */
public class UriDecodeTranslator implements IFileSystemTranslator {

    @Override
    public String toHSP(String file) throws IOException {
        try {
            return new org.apache.commons.codec.net.URLCodec().decode(file);
        } catch (DecoderException ex) {
            throw new IOException(ex);
        }
    }

    @Override
    public String fromHSP(String fileName) throws IOException {
        try {
            String convertedRoot = (fileName.charAt(0) == '\\') ? fileName.substring(1) : fileName;
            String convertedSlashes = convertedRoot.replace('\\', '/');
            return new URI(null, null, null, -1, convertedSlashes, null, null).toString();
        } catch (URISyntaxException ex) {
            throw new IOException(ex);
        }
    }
}
