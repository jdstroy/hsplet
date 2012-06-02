
import hsplet.Context;
import hsplet.HIMC;
import hsplet.HWND;
import java.awt.event.InputMethodEvent;
import java.awt.event.InputMethodListener;
import java.awt.im.InputContext;
import java.util.List;
import java.util.ListIterator;
import java.util.Map;
import java.util.TreeMap;
/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */

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
        for(int i = 0; i < size; i++) {
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
