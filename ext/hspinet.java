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

/**
 *
 * @author jdstroy
 */
public class hspinet extends FunctionBase {

    private Context context;

    public hspinet(final Context context) {
        this.context = context;
    }

    public int netinit(int a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netexec(String a, int b, int c, int d) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int neterror(@PEXInfo Operand pexinfo, int offset, int n, int m, int o) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int neturl(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netdlname(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public int netrequest(String str, int z, int a, int b) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
