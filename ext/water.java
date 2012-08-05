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

import hsplet.Application;
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.gui.Bmscr;
import java.util.logging.Level;
import java.util.logging.Logger;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jdstroy
 */
public class water extends FunctionBase {

    private Context context;

    public water(final Context context) {
        this.context = context;
    }
    
    public int water_getimage(int z, int a, int b, int c) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_getimage() called but unimplemented");
        return 0;
    }

    
    public int water_setripple(int a, int b, int c, int d) {
        //Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_setripple() called but unimplemented");
        return 0;
    }

    
    public int water_calc(int a, int b, int c, int d) {
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_calc() called but unimplemented");
        return 0;
    }

    
    public int water_draw(int bmscr_index, int a, int b, int c) {
        Bmscr el = context.windows.get(bmscr_index); // Just guessing here
        Logger.getLogger(getClass().getName()).log(Level.WARNING, "water_draw() called but unimplemented");
        return 0;
    }

}
