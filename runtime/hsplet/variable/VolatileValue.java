/*
 * $Id: VolatileValue.java,v 1.2.2.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;

import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;

/**
 * GUIコントロールなどに関連付けられた、リアルタイムに値が変更されるオブジェクト。
 * <p>
 * このクラスは別のオブジェクトを包含し、値の変更を検出、リアルタイムに更新することが出来る。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2.2.1 $, $Date: 2006/08/02 12:13:06 $
 */
public class VolatileValue extends Operand {

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = -835641208389109876L;

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: VolatileValue.java,v 1.2.2.1 2006/08/02 12:13:06 Yuki Exp $";

	/** 値を保持するオブジェクト。 */
	private Operand value;

	/** 値を更新するオブジェクト。 */
	public Set updaters = new HashSet();

	/**
	 * オブジェクトを構築する。
	 * 
	 * @param value 値を保持するオブジェクト。
	 */
	public VolatileValue(final Operand value) {

		this.value = value;
	}

	//@Override
	public int getType() {

		return value.getType();
	}

	//@Override
	public String toString(final int index) {

		update();
		return value.toString(index);
	}

	//@Override
	public ByteString toByteString(final int index) {

		update();
		return value.toByteString(index);
	}

	//@Override
	public int toInt(final int index) {

		update();

		return value.toInt(index);
	}

	//@Override
	public double toDouble(final int index) {

		update();

		return value.toDouble(index);
	}

	//@Override
	public Operand dup(int index) {
		return value.dup(index);
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

		update();

		value.inc(index);
	}

	//@Override
	public void dec(final int index) {

		update();

		value.inc(index);
	}

	//@Override
	public void assign(final int index, final Operand rhs, final int rhi) {

		value.assign(index, rhs, rhi);
	}

	//@Override
	public void assignAdd(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignAdd(index, rhs, rhi);
	}

	//@Override
	public void assignSub(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignSub(index, rhs, rhi);
	}

	//@Override
	public void assignMul(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignMul(index, rhs, rhi);
	}

	//@Override
	public void assignDiv(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignDiv(index, rhs, rhi);
	}

	//@Override
	public void assignMod(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignMod(index, rhs, rhi);
	}

	//@Override
	public void assignAnd(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignAnd(index, rhs, rhi);
	}

	//@Override
	public void assignOr(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignOr(index, rhs, rhi);
	}

	//@Override
	public void assignXor(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignXor(index, rhs, rhi);
	}

	//@Override
	public void assignSr(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignSr(index, rhs, rhi);
	}

	//@Override
	public void assignSl(final int index, final Operand rhs, final int rhi) {

		update();

		value.assignSl(index, rhs, rhi);
	}

	//@Override
	public Operand eq(final int index, final Operand rhs, final int rhi) {

		update();

		return value.eq(index, rhs, rhi);
	}

	//@Override
	public Operand ne(final int index, final Operand rhs, final int rhi) {

		update();

		return value.ne(index, rhs, rhi);
	}

	//@Override
	public Operand gt(final int index, final Operand rhs, final int rhi) {

		update();

		return value.gt(index, rhs, rhi);
	}

	//@Override
	public Operand lt(final int index, final Operand rhs, final int rhi) {

		update();

		return value.lt(index, rhs, rhi);
	}

	//@Override
	public Operand ge(final int index, final Operand rhs, final int rhi) {

		update();

		return value.ge(index, rhs, rhi);
	}

	//@Override
	public Operand le(final int index, final Operand rhs, final int rhi) {

		update();

		return value.le(index, rhs, rhi);
	}

	//@Override
	public Operand add(final int index, final Operand rhs, final int rhi) {

		update();

		return value.add(index, rhs, rhi);
	}

	//@Override
	public Operand sub(final int index, final Operand rhs, final int rhi) {

		update();

		return value.sub(index, rhs, rhi);
	}

	//@Override
	public Operand mul(final int index, final Operand rhs, final int rhi) {

		update();

		return value.mul(index, rhs, rhi);
	}

	//@Override
	public Operand div(final int index, final Operand rhs, final int rhi) {

		update();

		return value.div(index, rhs, rhi);
	}

	//@Override
	public Operand mod(final int index, final Operand rhs, final int rhi) {

		update();

		return value.mod(index, rhs, rhi);
	}

	//@Override
	public Operand and(final int index, final Operand rhs, final int rhi) {

		update();

		return value.and(index, rhs, rhi);
	}

	//@Override
	public Operand or(final int index, final Operand rhs, final int rhi) {

		update();

		return value.or(index, rhs, rhi);
	}

	//@Override
	public Operand xor(final int index, final Operand rhs, final int rhi) {

		update();

		return value.xor(index, rhs, rhi);
	}

	//@Override
	public Operand sl(final int index, final Operand rhs, final int rhi) {

		update();

		return value.sl(index, rhs, rhi);
	}

	//@Override
	public Operand sr(final int index, final Operand rhs, final int rhi) {

		update();

		return value.sr(index, rhs, rhi);
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

		update();

		return value.peek(index, offset);
	}

	//@Override
	public void poke(int index, int offset, byte value) {

		update();

		this.value.poke(index, offset, value);

	}

	private void update() {

		if (updaters.size() != 0) {

			for (final Iterator i = updaters.iterator(); i.hasNext();) {
				((VolatileValueUpdater) i.next()).update(value);
			}

			updaters.clear();
		}
	}

}
