package hsplet;

import hsplet.variable.*;

/**
 * Provides control flow information to the runtime
 * @author Kejardon
 */
public class FlagObject {

    public final int newTarget;
    public final Operand returnObject;
    public final boolean returnNow;

    public FlagObject(int i) {
        newTarget = i;
        returnNow = false;
        returnObject = null;
    }

    public FlagObject(Operand O) {
        returnObject = O;
        returnNow = true;
        newTarget = 0;
    }
    private static final FlagObject emptyFlagObject = new FlagObject(null);

    /**
     * Factory method for returning a value in HSPlet.
     * @param O value to return
     * @return a FlagObject that returns O
     */
    public static FlagObject getFO(Operand O) {
        if (O == null) {
            return emptyFlagObject;
        }
        return new FlagObject(O);
    }

    /**
     * Java friendly version of above factory - takes Java natives instead of Operands
     * @param val value to return
     * @return a FlagObject that returns val
     */
    public static FlagObject getFlagObject(String val) {
        return new FlagObject(Scalar.fromValue(val));
    }
    public static FlagObject getFlagObject(int val) {
        return new FlagObject(Scalar.fromValue(val));
    }
    public static FlagObject getFlagObject(double val) {
        return new FlagObject(Scalar.fromValue(val));
    }
    public static FlagObject getFlagObject() {
        return emptyFlagObject;
    }

    // Currently unused - intended for gotos, but currently gotos are combined
    // into methods and done directly in java bytecode
    /**
     * Factory method for setting a jump in HSPlet
     * @param i Label index to jump
     * @return a FlagObject that indicates a jump to the given Label index.
     */
    public static FlagObject getFO(int i) {
        return new FlagObject(i);
    }
}
