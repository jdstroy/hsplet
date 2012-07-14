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
	public String toString(final int index) {

		return value.toString(index + base);
	}

	//@Override
	public ByteString toByteString(final int index) {

		return value.toByteString(index + base);
	}

	//@Override
	public int toInt(final int index) {

		return value.toInt(index + base);
	}

	//@Override
	public double toDouble(final int index) {

		return value.toDouble(index + base);
	}

	//@Override
	public Operand dup(int index) {
		return value.dup(index + base);
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
	public void inc(final int index) {

		value.inc(index + base);
	}

	//@Override
	public void dec(final int index) {

		value.dec(index + base);
	}

	public void assign(final int index, final int newValue){
		value.assign(index + base, newValue);
	}
	public void assign(final int index, final double newValue){
		value.assign(index + base, newValue);
	}
	public void assign(final int index, final String newValue){
		value.assign(index + base, newValue);
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		value.assign(index + base, rhs, rhi);
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		value.assignAdd(index + base, rhs, rhi);
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		value.assignSub(index + base, rhs, rhi);
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		value.assignMul(index + base, rhs, rhi);
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		value.assignDiv(index + base, rhs, rhi);
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		value.assignMod(index + base, rhs, rhi);
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		value.assignAnd(index + base, rhs, rhi);
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		value.assignOr(index + base, rhs, rhi);
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		value.assignXor(index + base, rhs, rhi);
	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		value.assignSr(index + base, rhs, rhi);
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		value.assignSl(index + base, rhs, rhi);
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		return value.eq(index + base, rhs, rhi);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		return value.ne(index + base, rhs, rhi);
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		return value.gt(index + base, rhs, rhi);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		return value.lt(index + base, rhs, rhi);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		return value.ge(index + base, rhs, rhi);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		return value.le(index + base, rhs, rhi);
	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		return value.add(index + base, rhs, rhi);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		return value.sub(index + base, rhs, rhi);
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		return value.mul(index + base, rhs, rhi);
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		return value.div(index + base, rhs, rhi);
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		return value.mod(index + base, rhs, rhi);
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		return value.and(index + base, rhs, rhi);
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		return value.or(index + base, rhs, rhi);
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		return value.xor(index + base, rhs, rhi);
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		return value.sl(index + base, rhs, rhi);
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		return value.sr(index + base, rhs, rhi);
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
    public void assignNe(int index, Operand rhs, int rhi) {
        value.assignNe(index, rhs, rhi);
    }

    @Override
    public void assignGt(int index, Operand rhs, int rhi) {
        value.assignGt(index, rhs, rhi);
    }

    @Override
    public void assignLt(int index, Operand rhs, int rhi) {
        value.assignLt(index, rhs, rhi);
    }

    @Override
    public void assignGtEq(int index, Operand rhs, int rhi) {
        value.assignGtEq(index, rhs, rhi);
    }

    @Override
    public void assignLtEq(int index, Operand rhs, int rhi) {
        value.assignLtEq(index, rhs, rhi);
    }
}
