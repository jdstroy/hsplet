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

import hsplet.Context;
import hsplet.PEXInfo;
import hsplet.function.FunctionBase;
import hsplet.variable.Operand;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.yi.jdstroy.hsplet.compiler.interop.Out;

/*
Spec: http://www.onionsoft.net/hsp/v33/doclib/hspinet.txt
*/

/**
 *
 * @author jdstroy
 */
public class hspinet extends FunctionBase {

    private enum InitStatus {

        SUCCESS(0),
        FAILURE(1);

        public final int value;

        InitStatus(final int value) {
            this.value = value;
        }
    }

    private Context context;

    public hspinet(final Context context) {
        this.context = context;
    }

    /**
     * Called to initialize hspinet
     *
     * @return 0 on success; 1 on failure
     */
    public int netinit() {
        return InitStatus.SUCCESS.value;
    }

    /**
     * Called to terminate the session.
     */
    public void netterm() {

    }

    public int netexec(@Out Operand destination, int offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netmode(@Out Operand destination, int offset) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    private Throwable lastError;

    /**
     * Provides the last error encountered in hspinet.
     *
     * @param destination The destination to be written with the textual
     * representation of the error.
     * @param offset The offset from the array given by destination.
     */
    public void neterror(@PEXInfo @Out Operand destination, int offset) {
        destination.assign(offset, (lastError == null) ? "" : lastError.toString());
    }

    private URI base;

    /**
     * Sets the base URL for the next request.
     *
     * @param str The base of the URL
     */
    public int neturl(String str) {
        try {
            base = new URI(str);
            context.stat.assign(0);
            return InitStatus.SUCCESS.value;
        } catch (URISyntaxException ex) {
            Logger.getLogger(hspinet.class.getName()).log(Level.SEVERE, null, ex);
            context.stat.assign(1);
            return InitStatus.FAILURE.value;
        }
    }

    private String fileName;
    /**
     * Sets the filename that will be used for downloading the requested file.
     * @param filename The filename that will be used.
     */
    public void netdlname(final String filename) {
        this.fileName = filename;
    }

    /**
     * Performs the HTTP request. The caller must call neturl(String) first to
     * set the base URL. See also netexec() and netload().
     *
     * @param filename Filename of the HTTP request.
     */
    public int netrequest(String filename) {
        try {
            URI target = base.resolve(filename);
            // TODO: What else do we need to do here?
            target.toURL().openStream().close();
            // Do we need to set this.fileName to null?
            context.stat.assign(0);
            return InitStatus.SUCCESS.value;
        } catch (IllegalArgumentException ex) {
            context.stat.assign(1);
            return InitStatus.FAILURE.value;
        } catch (MalformedURLException ex) {
            Logger.getLogger(hspinet.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(hspinet.class.getName()).log(Level.SEVERE, null, ex);
        } finally {
            // Remove the next line when TODO is finished above
            throw new UnsupportedOperationException("Not supported yet.");
        }
    }
}
