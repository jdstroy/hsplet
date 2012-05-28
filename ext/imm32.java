
import hsplet.HIMC;
import hsplet.HWND;
import hsplet.variable.ByteString;

/*
 * To change this template, choose Tools | Templates and open the template in
 * the editor.
 */
/**
 *
 * @author jdstroy
 */
public class imm32 {

    /**
     * Gets the input context for the given HWND
     *
     * @param str
     * @return 
     */
    public int ImmGetContext(@HWND int windowId) {
        return 0;
    }

    public int ImmReleaseContext(@HWND int windowId, @HIMC int inputContextId) {
        return 0;
    }

    public int ImmSetOpenStatus(@HIMC int inputContextId, int fOpen) {
        return 0;
    }

    public int ImmGetOpenStatus(@HIMC int inputContextId) {
        return 0;
    }
}
