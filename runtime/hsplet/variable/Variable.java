/*
 * $Id: Variable.java,v 1.3.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import hsplet.HSPError;

/**
 * HSP の変数を表すクラス。
 * <p>
 * HSP の変数は dim/sdim や dup などによって型や参照先を動的に変更できなければいけない。
 * </p>
 * </p>
 * Array や Scalar などのサブクラスを直接使用するのではなく、一枚皮をかぶせる。
 * </p>
 * <p>
 * 本質的に Reference と同じだが、ベースインデックスが 0 であることを前提にしている分高速。 また、実際にはコンパイラによってインライン展開されるので、パフォーマンスの低下は最小限に抑えられる。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.3.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class Variable extends Operand {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 3981954990729087063L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Variable.java,v 1.3.2.1 2006/08/02 12:13:06 Yuki Exp $";
	//private static int globalErrorIndex=1;
	//public int myErrorIndex;

	/** 値を保持するオブジェクト。 */
	public Operand value;

	/**
	 * int 配列型の変数を構築する。
	 */
	public Variable() {

		//myErrorIndex=globalErrorIndex++;
		this.value = new IntArray();
	}

	/**
	 * 変数の値が外部から変更される可能性があるとみなす。
	 * @return 外部から変更される値オブジェクト.
	 */
	public VolatileValue makeVolatile() {

		if (value instanceof VolatileValue) {
			return (VolatileValue) value;
		}

		final VolatileValue result = new VolatileValue(value);
		this.value = result;
		return result;
	}

	//@Override
	public int getType() {

		return value.getType();
	}

	//@Override
	public Operand checkVar() {
		return this;
	}

	//@Override
	public String toStringRaw(final int index) {

		return value.toStringRaw(index);
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return value.toByteStringRaw(index);
	}

	//@Override
	public int toIntRaw(final int index) {

		return value.toIntRaw(index);
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return value.toDoubleRaw(index);
	}

	//@Override
	public Operand dupRaw(int index) {
		return value.dupRaw(index);
	}

	public void checkIncrementSize(int size) {
		value.checkIncrementSize(size);
	}
    public int checkSize0(int size) {
        return value.checkSize0(size);
    }
    public int checkSize1(int size) {
        return value.checkSize1(size);
    }
    public int checkSize2(int size) {
        return value.checkSize2(size);
    }
    public int checkSize3(int size) {
        return value.checkSize3(size);
    }

	public int checkResize0(int size) {
        return value.checkResize0(size);
	}
	public int checkResize1(int size) {
        return value.checkResize1(size);
	}
	public int checkResize2(int size) {
        return value.checkResize2(size);
	}
	public int checkResize3(int size) {
        return value.checkResize3(size);
	}
	//@Override
	public int getIndex(final int i0) {

		return value.getIndex(i0);
	}

	//@Override
	public int getIndex(final int i0, final int i1) {

		return value.getIndex(i0, i1);
	}

	//@Override
	public int getIndex(final int i0, final int i1, final int i2) {

		return value.getIndex(i0, i1, i2);
	}

	//@Override
	public int getIndex(final int i0, final int i1, final int i2, final int i3) {

		return value.getIndex(i0, i1, i2, i3);
	}

    //@Override
    public int getResizeIndex(final int i0) {

        return value.getResizeIndex(i0);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1) {

        return value.getResizeIndex(i0, i1);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2) {

        return value.getResizeIndex(i0, i1, i2);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {

        return value.getResizeIndex(i0, i1, i2, i3);
    }

	//@Override
	public void incRaw(final int index) {

		value.incRaw(index);
	}

	//@Override
	public void decRaw(final int index) {

		value.decRaw(index);
	}

	public void assignRaw(final int index, final int newValue) {
		if(value.getType() != Type.INTEGER) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new IntArray();
		}
		value.assignRaw(index, newValue);
	}
	public void assignRaw(final int index, final double newValue){
		if(value.getType() != Type.DOUBLE) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new DoubleArray();
		}
		value.assignRaw(index, newValue);
	}
	public void assignRaw(final int index, final String newValue){
		if(value.getType() != Type.STRING) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			value = new StringArray();
		}
		value.assignRaw(index, newValue);
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		// 型が違う代入、初期化される
		if (value.getType() != rhs.getType()) {
			if(index != 0) {
				error(HSPError.AssignToDifferentType, "", "Invalid type of array");
				return;
			}
			switch (rhs.getType()) {
			case Type.INTEGER:
				value = new IntArray();
				break;
			case Type.DOUBLE:
				value = new DoubleArray();
				break;
			case Type.STRING:
				value = new StringArray();
				break;
			default:
				throw new UnsupportedOperationException("型 " + rhs.getType() + " はサポートされていません。");
			}
		}

		value.assignRaw(index, rhs, rhi);
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAddRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSubRaw(index, rhs, rhi);
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		value.assignMulRaw(index, rhs, rhi);
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		value.assignDivRaw(index, rhs, rhi);
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		value.assignModRaw(index, rhs, rhi);
	}

	//@Override
	public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAndRaw(index, rhs, rhi);
	}

	//@Override
	public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignOrRaw(index, rhs, rhi);
	}

	//@Override
	public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

		value.assignXorRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSrRaw(index, rhs, rhi);
	}

	//@Override
	public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSlRaw(index, rhs, rhi);
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return value.eqRaw(index, rhs, rhi);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return value.neRaw(index, rhs, rhi);
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return value.gtRaw(index, rhs, rhi);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return value.ltRaw(index, rhs, rhi);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return value.geRaw(index, rhs, rhi);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return value.leRaw(index, rhs, rhi);
	}

	//@Override
	public Operand addRaw(final int index, final Operand rhs, final int rhi) {

		return value.addRaw(index, rhs, rhi);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		return value.subRaw(index, rhs, rhi);
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		return value.mulRaw(index, rhs, rhi);
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		return value.divRaw(index, rhs, rhi);
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		return value.modRaw(index, rhs, rhi);
	}

	//@Override
	public Operand andRaw(final int index, final Operand rhs, final int rhi) {

		return value.andRaw(index, rhs, rhi);
	}

	//@Override
	public Operand orRaw(final int index, final Operand rhs, final int rhi) {

		return value.orRaw(index, rhs, rhi);
	}

	//@Override
	public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

		return value.xorRaw(index, rhs, rhi);
	}

	//@Override
	public Operand slRaw(final int index, final Operand rhs, final int rhi) {

		return value.slRaw(index, rhs, rhi);
	}

	//@Override
	public Operand srRaw(final int index, final Operand rhs, final int rhi) {

		return value.srRaw(index, rhs, rhi);
	}

	//@Override
	public int l0() {

		return value.l0();
	}

	//@Override
	public int l1() {

		return value.l1();
	}

	//@Override
	public int l2() {

		return value.l2();
	}

	//@Override
	public int l3() {

		return value.l3();
	}

	//@Override
	public byte peek(int index, int offset) {

		return value.peek(index, offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value.poke(index, offset, value);

	}

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        value.assignNeRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        value.assignGtRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        value.assignLtRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        value.assignGtEqRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        value.assignLtEqRaw(index, rhs, rhi);
    }

}
