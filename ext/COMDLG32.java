
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import javax.swing.JFileChooser;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

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
