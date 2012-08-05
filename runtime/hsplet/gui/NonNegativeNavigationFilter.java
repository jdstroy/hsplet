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

package hsplet.gui;

import javax.swing.text.BadLocationException;
import javax.swing.text.JTextComponent;
import javax.swing.text.NavigationFilter;
import javax.swing.text.Position.Bias;

/**
 *
 * @author jdstroy
 */
public class NonNegativeNavigationFilter extends NavigationFilter {

    @Override
    public int getNextVisualPositionFrom(JTextComponent text, int pos, Bias bias, int direction, Bias[] biasRet) throws BadLocationException {
        return super.getNextVisualPositionFrom(text, pos, bias, direction, biasRet);
    }

    @Override
    public void moveDot(FilterBypass fb, int dot, Bias bias) {
        if (dot > -1) {
            super.moveDot(fb, dot, bias);
        }
    }

    @Override
    public void setDot(FilterBypass fb, int dot, Bias bias) {
        if (dot > -1) {
            super.setDot(fb, dot, bias);
        }
    }
}
