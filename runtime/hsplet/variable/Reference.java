/*
 * $Id: Reference.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

/**
 * 別のオペランドの特定のインデックスを参照するオペランドを表すクラス。
 * <p>
 * dup で特定の配列要素に変数が割り当てられた場合などに使用する。
 * </p>
 * <p>
 * すべての呼び出しに対してベースインデックスの下駄を履かせて参照先に処理を委譲する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class Reference extends Operand {

	/* TODO:
	 * This does not implement dup frop HSP correctly.
	 * At the time the reference is made, it calculates the Operand's available size(total size - base)
	 * and saves this as the reference's l0 (l1, l2, and l3 are always 0 for a reference). A reference
	 * may not be resized (getResizeIndex should act the same as getIndex).
	 * Resizing the reference's target will break the link (the reference will still work the same but
	 * changes in one will not affect the other). That behaviour is correctly imitated here already and
	 * should be preserved.
	 */
	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -6159793912128847879L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: Reference.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";

	/** 値を保持するオブジェクト。 */
	private Operand value;

	/** ベースインデックス。 */
	private int base;

	/**
	 * オブジェクトを構築する。
	 * 
	 * @param value 値を保持するオブジェクト。
	 * @param base オブジェクトのベースインデックス。
	 */
	public Reference(final Operand value, final int base) {

		this.value = value;
		this.base = base;
	}

	//@Override
	public int getType() {

		return value.getType();
	}

	//@Override
	public Operand checkVar() {
		value.checkVar();
		return this;
	}

	//@Override
	public String toStringRaw(final int index) {

		return value.toStringRaw(index + base);
	}

	//@Override
	public ByteString toByteStringRaw(final int index) {

		return value.toByteStringRaw(index + base);
	}

	//@Override
	public int toIntRaw(final int index) {

		return value.toIntRaw(index + base);
	}

	//@Override
	public double toDoubleRaw(final int index) {

		return value.toDoubleRaw(index + base);
	}

	//@Override
	public Operand dupRaw(int index) {
		return value.dupRaw(index + base);
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

	//Ref may not actually 
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

		value.incRaw(index + base);
	}

	//@Override
	public void decRaw(final int index) {

		value.decRaw(index + base);
	}

	public void assignRaw(final int index, final int newValue){
		value.assignRaw(index + base, newValue);
	}
	public void assignRaw(final int index, final double newValue){
		value.assignRaw(index + base, newValue);
	}
	public void assignRaw(final int index, final String newValue){
		value.assignRaw(index + base, newValue);
	}

	//@Override
	public void assignRaw(final int index, final Operand rhs, final int rhi) {

		value.assignRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAddRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSubRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

		value.assignMulRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

		value.assignDivRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignModRaw(final int index, final Operand rhs, final int rhi) {

		value.assignModRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

		value.assignAndRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignOrRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

		value.assignXorRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSrRaw(index + base, rhs, rhi);
	}

	//@Override
	public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

		value.assignSlRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

		return value.eqRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand neRaw(final int index, final Operand rhs, final int rhi) {

		return value.neRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

		return value.gtRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

		return value.ltRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand geRaw(final int index, final Operand rhs, final int rhi) {

		return value.geRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand leRaw(final int index, final Operand rhs, final int rhi) {

		return value.leRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand addRaw(final int index, final Operand rhs, final int rhi) {

		return value.addRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand subRaw(final int index, final Operand rhs, final int rhi) {

		return value.subRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

		return value.mulRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand divRaw(final int index, final Operand rhs, final int rhi) {

		return value.divRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand modRaw(final int index, final Operand rhs, final int rhi) {

		return value.modRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand andRaw(final int index, final Operand rhs, final int rhi) {

		return value.andRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand orRaw(final int index, final Operand rhs, final int rhi) {

		return value.orRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

		return value.xorRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand slRaw(final int index, final Operand rhs, final int rhi) {

		return value.slRaw(index + base, rhs, rhi);
	}

	//@Override
	public Operand srRaw(final int index, final Operand rhs, final int rhi) {

		return value.srRaw(index + base, rhs, rhi);
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

		return value.peek(index + base, offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		this.value.poke(index + base, offset, value);

	}

	//@Override
	public Operand ref(int index) {

		if (index == 0) {
			return this;
		} else {
			return new Reference(value, base + index);
		}
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
