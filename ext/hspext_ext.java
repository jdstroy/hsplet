
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;
import interfaces.IHSPExtExt;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jdstroy
 */
public class hspext_ext extends FunctionBase {

    private Context context;

    public hspext_ext(final Context context) {
        this.context = context;
    }

    public void apledit(Operand numericTarget, int index, int meaningOfC, int rowIndex) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aplobj(String objname, int a) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void aplsel(Operand destination, int index, String name, int startId) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void ematan(Operand destination, int index, double x, double y) {
        destination.add(index, Scalar.fromValue(Math.atan2(y, x)), 0);
    }

    public void gfdec(int red, int green, int blue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void gfdec2(int red, int green, int blue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void gfinc(int red, int green, int blue) {
        throw new UnsupportedOperationException("Not supported yet.");
    }

    public void gfini(int x, int y) {
        throw new UnsupportedOperationException("Not supported yet.");
    }
}
