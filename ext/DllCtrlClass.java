
import hsplet.Context;
import hsplet.function.FunctionBase;
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

    /**
     * Converts source (buffer containing wide data) to a HSP string
     * @param index Offset from base address of source buffer to begin reading wide data
     * @param source Source buffer containing wide character data 
     * @return Externally-usable Unicode string
     */
    public String cnvwtos(Operand source, int index) {
        return source.toByteString(index).toString();
    }

    public int comevdisp(Operand p1, int ip2) {
        return -1;
    }

    public int delcom(Operand p1, int ip1) {
        return -1;
    }

    /**
     * Converts source to "unicode" -- this probably means a format like UTF8, 
     * or UTF 16
     * @param target Destination buffer for storing encoded Unicode data
     * @param index Offset from base address of target buffer to begin storing Unicode data
     * @param source Source string containing data to convert
     * @return unknown
     */
    public void cnvstow(Operand target, int index, String source) {
        target.assign(index, Scalar.fromValue(source), 0);
    }

    public int axobj(Operand p1, int ip1, String name, int value1, int value2) {
        return -1;
    }

    public int comevent(Operand p1, int ip1, Operand p2, int ip2, String guid, Operand label, int labelIndex) {
        return -1;
    }
}
