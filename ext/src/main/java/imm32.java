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
import hsplet.HIMC;
import hsplet.HWND;
import java.awt.im.InputContext;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 *
 * @author jdstroy
 */
public class imm32 {

    private Context context;

    public imm32(Context context) {
        this.context = context;
    }
    private Map<Integer, InputContext> handles = new TreeMap<>();

    /**
     * Gets the input context for the given HWND.
     *
     * @param windowId The window ID associated with the InputContext
     * @return
     */
    public int ImmGetContext(@HWND int windowId) {
        List<InputContext> li = context.getInputContexts();
        int size = li.size();
        for (int i = 0; i < size; i++) {
            if (li.get(i) == null) {
                li.set(i, InputContext.getInstance());
                return i;
            }
        }
        li.add(InputContext.getInstance());
        //context.windows.get(windowId).component.addInputMethodListener();
        return size;
    }

    public int ImmReleaseContext(@HWND int windowId, @HIMC int inputContextId) {
        try {
            List<InputContext> li = context.getInputContexts();
            InputContext ctx = li.get(inputContextId);
            ctx.endComposition();
            ctx.dispose();
            li.set(inputContextId, null);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    public int ImmSetOpenStatus(@HIMC int inputContextId, int fOpen) {
        try {
            List<InputContext> li = context.getInputContexts();
            InputContext ctx = li.get(inputContextId);
            ctx.setCompositionEnabled(fOpen == 1);
            return 1;
        } catch (Exception ex) {
            return 0;
        }
    }

    public int ImmGetOpenStatus(@HIMC int inputContextId) {
        List<InputContext> li = context.getInputContexts();
        InputContext ctx = li.get(inputContextId);
        return ctx.isCompositionEnabled() ? 1 : 0;
    }
}
