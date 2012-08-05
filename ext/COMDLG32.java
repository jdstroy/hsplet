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
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import javax.swing.JFileChooser;

/**
 *
 * @author jdstroy
 */
public class COMDLG32 extends FunctionBase {

    private Context context;

    public COMDLG32(final Context context) {
        this.context = context;
    }

    public int GetOpenFileNameA(ByteString sptr) {
        JFileChooser ch = new JFileChooser(sptr.toString());
        if (ch.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            sptr.assign(ch.getSelectedFile().toString());
            return 1;
        }
        return 0;
    }

    public int GetSaveFileNameA(ByteString sptr) {
        JFileChooser ch = new JFileChooser(sptr.toString());
        if (ch.showSaveDialog(null) == JFileChooser.APPROVE_OPTION) {
            sptr.assign(ch.getSelectedFile().toString());
            return 1;
        }
        return 0;
    }
}
