
import hsplet.Context;
import hsplet.HSPError;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import interfaces.IHSPDA;
import java.io.Serializable;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jdstroy
 */
public class hspda extends FunctionBase implements Serializable {

    private Context context;

    public hspda(final Context context) {
        this.context = context;
    }

    public int sortnote(Operand pexinfo, int index, int sortDirection) {
        errorCurrentMethod(context);
        return 0;
    }

    private void errorCurrentMethod(Context ctx) {
        ctx.error(HSPError.UnsupportedOperation, Thread.currentThread().getStackTrace()[1].getMethodName());
    }

    public int xnotesel(String names, int size) {
        final ByteString note = context.note.toByteString(0);
        errorCurrentMethod(context);
        return 0;
    }

    public int xnoteadd(String note) {
        errorCurrentMethod(context);
        return 0;
    }

}
