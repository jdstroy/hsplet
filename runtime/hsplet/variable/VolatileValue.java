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
    public Set<VolatileValueUpdater> updaters = new HashSet<VolatileValueUpdater>();

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
    public String toStringRaw(final int index) {

        update();
        return value.toStringRaw(index);
    }

    //@Override
    public ByteString toByteStringRaw(final int index) {

        update();
        return value.toByteStringRaw(index);
    }

    //@Override
    public int toIntRaw(final int index) {

        update();

        return value.toIntRaw(index);
    }

    //@Override
    public double toDoubleRaw(final int index) {

        update();

        return value.toDoubleRaw(index);
    }

    //@Override
    public Operand dupRaw(int index) {
        update();
        return value.dupRaw(index);
    }

    public void checkIncrementSize(int size) {
        update();
        value.checkIncrementSize(size);
    }
    public int checkSize0(int size) {
        update();
        return value.checkSize0(size);
    }
    public int checkSize1(int size) {
        update();
        return value.checkSize1(size);
    }
    public int checkSize2(int size) {
        update();
        return value.checkSize2(size);
    }
    public int checkSize3(int size) {
        update();
        return value.checkSize3(size);
    }
	public int checkResize0(int size) {
        update();
        return value.checkResize0(size);
	}
	public int checkResize1(int size) {
        update();
        return value.checkResize1(size);
	}
	public int checkResize2(int size) {
        update();
        return value.checkResize2(size);
	}
	public int checkResize3(int size) {
        update();
        return value.checkResize3(size);
	}
    //@Override
    public int getIndex(final int i0) {
        update();
        return value.getIndex(i0);
    }

    //@Override
    public int getIndex(final int i0, final int i1) {
        update();
        return value.getIndex(i0, i1);
    }

    //@Override
    public int getIndex(final int i0, final int i1, final int i2) {
        update();
        return value.getIndex(i0, i1, i2);
    }

    //@Override
    public int getIndex(final int i0, final int i1, final int i2, final int i3) {
        update();
        return value.getIndex(i0, i1, i2, i3);
    }

    //@Override
    public int getResizeIndex(final int i0) {
        update();
        return value.getResizeIndex(i0);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1) {
        update();
        return value.getResizeIndex(i0, i1);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2) {
        update();
        return value.getResizeIndex(i0, i1, i2);
    }

    //@Override
    public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {
        update();
        return value.getResizeIndex(i0, i1, i2, i3);
    }

    //@Override
    public void incRaw(final int index) {

        update();

        value.incRaw(index);
    }

    //@Override
    public void decRaw(final int index) {

        update();

        value.decRaw(index);
    }

    public void assignRaw(final int index, final int newValue){
        update();
        value.assignRaw(index, newValue);
    }
    public void assignRaw(final int index, final double newValue){
        update();
        value.assignRaw(index, newValue);
    }
    public void assignRaw(final int index, final String newValue){
        update();
        value.assignRaw(index, newValue);
    }

    //@Override
    public void assignRaw(final int index, final Operand rhs, final int rhi) {
        update();
        value.assignRaw(index, rhs, rhi);
    }

    //@Override
    public void assignAddRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignAddRaw(index, rhs, rhi);
    }

    //@Override
    public void assignSubRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignSubRaw(index, rhs, rhi);
    }

    //@Override
    public void assignMulRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignMulRaw(index, rhs, rhi);
    }

    //@Override
    public void assignDivRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignDivRaw(index, rhs, rhi);
    }

    //@Override
    public void assignModRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignModRaw(index, rhs, rhi);
    }

    //@Override
    public void assignAndRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignAndRaw(index, rhs, rhi);
    }

    //@Override
    public void assignOrRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignOrRaw(index, rhs, rhi);
    }

    //@Override
    public void assignXorRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignXorRaw(index, rhs, rhi);
    }

    //@Override
    public void assignSrRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignSrRaw(index, rhs, rhi);
    }

    //@Override
    public void assignSlRaw(final int index, final Operand rhs, final int rhi) {

        update();

        value.assignSlRaw(index, rhs, rhi);
    }

    //@Override
    public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.eqRaw(index, rhs, rhi);
    }

    //@Override
    public Operand neRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.neRaw(index, rhs, rhi);
    }

    //@Override
    public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.gtRaw(index, rhs, rhi);
    }

    //@Override
    public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.ltRaw(index, rhs, rhi);
    }

    //@Override
    public Operand geRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.geRaw(index, rhs, rhi);
    }

    //@Override
    public Operand leRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.leRaw(index, rhs, rhi);
    }

    //@Override
    public Operand addRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.addRaw(index, rhs, rhi);
    }

    //@Override
    public Operand subRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.subRaw(index, rhs, rhi);
    }

    //@Override
    public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.mulRaw(index, rhs, rhi);
    }

    //@Override
    public Operand divRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.divRaw(index, rhs, rhi);
    }

    //@Override
    public Operand modRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.modRaw(index, rhs, rhi);
    }

    //@Override
    public Operand andRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.andRaw(index, rhs, rhi);
    }

    //@Override
    public Operand orRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.orRaw(index, rhs, rhi);
    }

    //@Override
    public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.xorRaw(index, rhs, rhi);
    }

    //@Override
    public Operand slRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.slRaw(index, rhs, rhi);
    }

    //@Override
    public Operand srRaw(final int index, final Operand rhs, final int rhi) {

        update();

        return value.srRaw(index, rhs, rhi);
    }

    //@Override
    public int l0() {
        update();
        return value.l0();
    }

    //@Override
    public int l1() {
        update();
        return value.l1();
    }

    //@Override
    public int l2() {
        update();
        return value.l2();
    }

    //@Override
    public int l3() {
        update();
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

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        update();
        value.assignNeRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        update();
        value.assignGtRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        update();
        value.assignLtRaw(index, rhs, rhi);
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        update();
        value.assignGtEqRaw(index, rhs, rhi);
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        update();
        value.assignLtEqRaw(index, rhs, rhi);
    }
}
