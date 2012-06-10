/*
 * $Id: ProgramCommand.java,v 1.6 2006/01/29 16:29:20 Yuki Exp $
 */
package hsplet.function;

import hsplet.Context;
import hsplet.HSPError;
import hsplet.Task;
import hsplet.variable.DoubleArray;
import hsplet.variable.IntArray;
import hsplet.variable.Operand;
import hsplet.variable.StringArray;
import hsplet.variable.Variable;

/**
 * HSP のプログラム制御コマンド群。
 * 
 * @author Yuki
 * @version $Revision: 1.6 $, $Date: 2006/01/29 16:29:20 $
 */
public class ProgramCommand extends FunctionBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: ProgramCommand.java,v 1.6 2006/01/29 16:29:20 Yuki Exp $";

	public static void goto_(final Context context, final int label, final boolean clearLoop) {

		if (clearLoop) {
			context.looplev.value = 0;
		}
		throw new GotoException(label);
	}

	public static void gosub(final Context context, final int label) {

		++context.sublev.value;
		try {
			Operand O=context.getRunnableCode().run(label);
			if(O!=null) context.stat.value=O.toInt(0);
		} finally {
			--context.sublev.value;
		}
	}

	public static Operand call(final Operand[] arguments, final Context context, final int label) {
		++context.sublev.value;
		context.addArguments(arguments);
		try {
			return context.getRunnableCode().run(label);
		} finally {
			--context.sublev.value;
			context.popArguments();
		}
	}

	private static int waitCount = 0;

	public static void wait(final Context context, final Operand v, final int vi) {

		++waitCount;
		if ((waitCount % 10000) == 0) {
			System.gc();
		}

		doTasks(context);

		final int waitTime = toInt(v, vi, context.lastWaitDuration);

		if (waitTime < 0) {
			context.error(HSPError.InvalidParameterValue, "wait", "duration==" + waitTime);
			return;
		}

		context.lastWaitDuration = waitTime;

		try {
			Thread.sleep(Math.max(5, context.lastWaitDuration * 10));
		} catch (InterruptedException e) {
			e.printStackTrace();
		}

		context.lastAwaitTime = System.currentTimeMillis();
	}

	public static void await(final Context context, final Operand v, final int vi) {

		++waitCount;
		if ((waitCount % 10000) == 0) {
			System.gc();
		}

		doTasks(context);

		final int waitTime = toInt(v, vi, 0);

		if (waitTime < 0) {
			context.error(HSPError.InvalidParameterValue, "wait", "duration==" + waitTime);
			return;
		}

		final long duration = context.lastAwaitTime + waitTime - System.currentTimeMillis();

		try {
			if (duration > 0) {

				Thread.sleep(duration);
			} else {
				Thread.sleep(0);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		context.lastAwaitTime = System.currentTimeMillis();

	}

	private static void doTasks(final Context context) {

		while (!context.tasks.isEmpty()) {

			final Task task = context.tasks.get(0);
			context.tasks.remove(0);

			task.run(context);

		}
	}

	public static void dim(final Context context, final Operand v, final int vi, final int l0, final int l1,
			final int l2, final int l3) {

		if (!(v instanceof Variable)) {
			context.error(HSPError.ParameterTypeMismatch, "dim", "typeof( v )==" + v.getClass().getName());
			return;
		}

		((Variable) v).value = new IntArray(Math.max(1, l0), Math.max(1, l1), Math.max(1, l2), Math.max(1, l3));

	}

	public static void sdim(final Context context, final Operand v, final int vi, final Operand lengthv,
			final int lengthvi, final int l0, final int l1, final int l2, final int l3) {

		if (!(v instanceof Variable)) {
			context.error(HSPError.ParameterTypeMismatch, "sdim", "typeof( v )==" + v.getClass().getName());
			return;
		}

		((Variable) v).value = new StringArray(toInt(lengthv, lengthvi, 64), Math.max(1, l0), Math.max(1, l1), Math
				.max(1, l2), Math.max(1, l3));

	}

	public static void dimtype(final Context context, final Operand v, final int vi, final int type, final int l0,
			final int l1, final int l2, final int l3) {

		if (!(v instanceof Variable)) {
			context.error(HSPError.ParameterTypeMismatch, "dimtype", "typeof( v )==" + v.getClass().getName());
			return;
		}

		switch (type) {
		case Operand.Type.INTEGER:
			((Variable) v).value = new IntArray(Math.max(1, l0), Math.max(1, l1), Math.max(1, l2), Math.max(1, l3));
			break;
		case Operand.Type.DOUBLE:
			((Variable) v).value = new DoubleArray(Math.max(1, l0), Math.max(1, l1), Math.max(1, l2), Math.max(1, l3));
			break;
		case Operand.Type.STRING:
			((Variable) v).value = new StringArray(64, Math.max(1, l0), Math.max(1, l1), Math.max(1, l2), Math.max(1,
					l3));
			break;

		}

	}

	public static void dup(final Context context, final Operand v, final int vi, final Operand v2, final int v2i) {

		if (!(v instanceof Variable)) {
			context.error(HSPError.ParameterTypeMismatch, "dup", "typeof( v )==" + v.getClass().getName());
			return;
		}

		final Operand ref = v2.ref(v2i);

		((Variable) v).value = ref instanceof Variable ? ((Variable) ref).value : ref;

	}

	public static void dupptr(final Context context, final Operand v, final int vi) {

		context.error(HSPError.UnsupportedOperation, "dupptr");

	}

	public static void end(final Context context, final int exitCode) {

		try {
			System.exit(exitCode);
		} catch (Exception e) {

		}
		throw new EndException();
	}

	public static void stop(final Context context) {

		try {
			while (true) {
				++waitCount;
				if ((waitCount % 10000) == 0) {
					System.gc();
				}
				doTasks(context);
				Thread.sleep(100);
			}
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static void newmod(final Context context, final Operand v, final int vi) {

		// TODO newmod
		context.error(HSPError.UnsupportedOperation, "newmod");

	}

	public static void setmod(final Context context, final Operand v, final int vi) {

		// TODO setmod
		context.error(HSPError.UnsupportedOperation, "strmod");

	}

	public static void delmod(final Context context, final Operand v, final int vi) {

		// TODO delmod
		context.error(HSPError.UnsupportedOperation, "delmod");

	}

	public static void alloc(final Context context, final int label) {

		// alloc 命令はマクロになったので呼び出されることは無い。
		context.error(HSPError.UnsupportedOperation, "alloc");
	}

	public static void mref(final Context context, final Operand v, final int vi, final int type) {

		if (!(v instanceof Variable)) {
			context.error(HSPError.ParameterTypeMismatch, "mref", "typeof( v )==" + v.getClass().getName());
			return;
		}

		//System.out.println(((Variable)v).errorIndex()+" Context variable");
		switch (type) {
		case 64:
			((Variable) v).value = context.stat;
			break;
		case 65:
			((Variable) v).value = context.refdval;
			break;
		}
	}

	public static void run(final Context context, final String axName, final String cmdline) {

		if (axName == null) {
			context.error(HSPError.ParameterCannotBeOmitted, "run", "axName==");
			return;
		}

		try {
			throw new RunException(Class.forName(axName.toLowerCase().endsWith(".ax") ? axName.substring(0, axName
					.length()
					- ".ax".length()) : axName), cmdline);
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
			context.error(HSPError.ErrorOnExecution, "run", axName);
		}

	}

	public static void exgoto(final Context context, final int a, final int mode, final int b, final int label) {

		if (mode >= 0) {
			if (a >= b) {
				goto_(context, label, false);
			}
		} else {
			if (a <= b) {
				goto_(context, label, false);
			}
		}
	}

	public static void on(final Context context, final int index, final JumpStatement jump, final int[] labels) {

		if (index >= 0 && index < labels.length) {
			if (jump == null || jump == JumpStatement.Goto) {
				goto_(context, labels[index], false);
			} else {
				gosub(context, labels[index]);

			}
		}
	}

	//this may not be right for other projects, but it is all Elona uses
	public static void mcall(final Context context, final Operand v, final int vi, String methodName, final Operand methodArg, final int methodArgi) {

		context.error(HSPError.UnsupportedOperation, "mcall");

	}

	public static void assert_(final Context context, final Operand v, final int vi) {

		context.error(HSPError.UnsupportedOperation, "assert");

	}

	public static void logmes(final Context context, final String text) {

		System.out.println(text);

	}

}
