
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.ByteString;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
/**
 *
 * @author jdstroy
 */
public class DllCtrlClass extends FunctionBase {

    private Context context;

    public DllCtrlClass(final Context context) {
        this.context = context;
    }

    public int callfunc(Operand args, int index, int funcAddress, int numParams) {
        return -1;
    }

    public int cnvwtos(Operand destination, int index, String str) {
        destination.assign(index, Scalar.fromValue(str.charAt(0)), 0);
        return 1;
    }

    public int comevdisp(Operand p1, int ip2) {
        return -1;
    }

    public int delcom(Operand p1, int ip1) {
        return -1;
    }

    public int cnvstow(ByteString bs1, ByteString bs2) {
        return -1;
    }

    public int axobj(Operand p1, int ip1, String name, int value1, int value2) {
        return -1;
    }

    public int comevent(Operand p1, int ip1, Operand p2, int ip2, String guid, Operand label, int labelIndex) {
        return -1;
    }
}
