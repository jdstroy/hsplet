/*
 * Copyright 2012 John Stroy
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *     http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
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

    public int exrand_rnd(Operand destination, int index, int max) {
        double tc = r.nextDouble();
	if (max < 0) return 3;
        int value = (int) (tc * (double)max);
        destination.assign(index, Scalar.fromValue(value), 0);
	return 0;
    }
}
