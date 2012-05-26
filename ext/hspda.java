
import hsplet.Context;
import hsplet.HSPError;
import hsplet.function.BasicCommand;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import hsplet.variable.StringArray;
import hsplet.variable.Variable;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

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
    
    //public int sortnote(Operand pexinfo, int index, int sortDirection) {
    public int sortnote(Operand bs_contents, final int offset, final int sortDirection) {
        assert(offset == 0); // Is this correct?
        ByteString contents = bs_contents.toByteString(offset);
        int size = contents.lineCount();
        ArrayList<String> li = new ArrayList<String>();
        BasicCommand.noteget(context, bs_contents, size, size);
        for (int i = 0; i < size; i++) {
            li.add(contents.getLine(i));
        }
        Comparator<String> c = new Comparator<String>() {

            @Override
            public int compare(String o1, String o2) {
                // May need to reverse order
                return o1.compareTo(o2) * ((sortDirection > 0) ? 1 : -1);
            }
        };
        Collections.sort(li, c);
        
        for (int i = 0; i < size; i++) {
            BasicCommand.noteadd(context, new ByteString(li.get(i)), bs_contents, i, 1);
        }
        
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
