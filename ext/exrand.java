/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */

/**
 *
 * @author jdstroy
 */
import hsplet.Context;
import hsplet.function.FunctionBase;
import hsplet.variable.Operand;
import hsplet.variable.Scalar;

import java.io.Serializable;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.Date;
import java.util.Random;

public class exrand extends FunctionBase implements Serializable {

    private Context context;

    public exrand(final Context context) {
        this.context = context;
    }

    private Random r = new Random();

    public int exrand_randomize(int seed) {
        if (seed < 0)
            return 3;
        r.setSeed(seed);
        return 0;
    }

    public int exrand_randomize_by_time() {
        r.setSeed(new Date().getTime());
        return 0;
    }

    public int exrand_rnd(Operand a, int index, int max) {
        double tc = r.nextDouble();
	if (max < 0) return 3;
	tc = r.nextDouble();
        int value = (int) (tc * (double)max);
        a.assign(index, Scalar.fromLabel(value), 0);
	return 0;
    }
}
