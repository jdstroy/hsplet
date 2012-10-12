/*
 * $Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $
 */
package hsplet.variable;
import hsplet.util.Conversion;

/**
 * int �̔z���\���N���X�B
 * 
 * 
 * @author Yuki
 * @version $Revision: 1.2.4.1 $, $Date: 2006/08/02 12:13:06 $
 */
public final class IntArray extends Array {

    /** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
    private static final long serialVersionUID = -4406165790569093040L;
    /** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
    private static final String fileVersionID = "$Id: IntArray.java,v 1.2.4.1 2006/08/02 12:13:06 Yuki Exp $";
    /** �I�u�W�F�N�g�̒l�B */
    private int[] values;

    /**
     * �v�f�� 16 �̔z����\�z����B
     */
    public IntArray() {

        this(1, 0, 0, 0);
    }

    /**
     * �v�f�����w�肵�ăI�u�W�F�N�g���\�z����B
     * <p>
     * �I���W�i�� HSP �ƈႢ�A�v�f���� 0 ���w�肷�邱�Ƃ͏o���Ȃ��B �܂�A�z��͕K���l�������邱�ƂɂȂ�B ���̂ق����v�f���Ȃǂ̌v�Z���ȒP�B
     * </p>
     *
     * @param l0 �ꎟ���ڂ̗v�f���B
     * @param l1 �񎟌��ڂ̗v�f���B
     * @param l2 �O�����ڂ̗v�f���B
     * @param l3 �l�����ڂ̗v�f���B
     */
    public IntArray(final int l0, final int l1, final int l2, final int l3) {

        super(l0, l1, l2, l3);
		int size=l0;
		if(l1>0) {
			size *= l1;
			if(l2>0) {
				size*=l2;
				if(l3>0) {
					size*=l3;
				}
			}
		}
        values = new int[size];
    }

    @Override
    public int getType() {

        return Type.INTEGER;
    }

    @Override
    public String toStringRaw(final int index) {

        return Integer.toString(values[index]);
    }

    @Override
    public ByteString toByteStringRaw(final int index) {

        return new ByteString(Integer.toString(values[index]));
    }

    @Override
    public int toIntRaw(final int index) {

        return values[index];
    }

    @Override
    public double toDoubleRaw(final int index) {

        return values[index];
    }

    @Override
    public Operand dupRaw(int index) {
        return Scalar.fromValue(values[index]);
    }

    @Override
    public void incRaw(final int index) {

        ++values[index];
    }

    @Override
    public void decRaw(final int index) {

        --values[index];
    }

    public void assignRaw(final int index, final int newValue){
        values[index] = newValue;
    }
    public void assignRaw(final int index, final double newValue){
        values[index] = (int)newValue;
    }
    public void assignRaw(final int index, final String newValue){
        values[index] = Conversion.strtoi(newValue);;
    }

    @Override
    public void assignRaw(final int index, final Operand rhs, final int rhi) {
        values[index] = rhs.toIntRaw(rhi);
    }

    @Override
    public void assignAddRaw(final int index, final Operand rhs, final int rhi) {
        values[index] += rhs.toIntRaw(rhi);
    }

    @Override
    public void assignSubRaw(final int index, final Operand rhs, final int rhi) {
        values[index] -= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignMulRaw(final int index, final Operand rhs, final int rhi) {
        values[index] *= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignDivRaw(final int index, final Operand rhs, final int rhi) {
        values[index] /= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignModRaw(final int index, final Operand rhs, final int rhi) {
        values[index] %= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignAndRaw(final int index, final Operand rhs, final int rhi) {
        values[index] &= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignOrRaw(final int index, final Operand rhs, final int rhi) {
        values[index] |= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignXorRaw(final int index, final Operand rhs, final int rhi) {
        values[index] ^= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignSrRaw(final int index, final Operand rhs, final int rhi) {
        values[index] >>= rhs.toIntRaw(rhi);
    }

    @Override
    public void assignSlRaw(final int index, final Operand rhs, final int rhi) {
        values[index] <<= rhs.toIntRaw(rhi);
    }

    @Override
    public Operand addRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] + rhs.toIntRaw(rhi));
    }

    @Override
    public Operand eqRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] == rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand neRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] != rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand gtRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] > rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand ltRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] < rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand geRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] >= rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand leRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue((values[index] <= rhs.toIntRaw(rhi)) ? 1 : 0);
    }

    @Override
    public Operand subRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] - rhs.toIntRaw(rhi));
    }

    @Override
    public Operand mulRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] * rhs.toIntRaw(rhi));
    }

    @Override
    public Operand divRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] / rhs.toIntRaw(rhi));
    }

    @Override
    public Operand modRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] % rhs.toIntRaw(rhi));
    }

    @Override
    public Operand andRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] & rhs.toIntRaw(rhi));
    }

    @Override
    public Operand orRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] | rhs.toIntRaw(rhi));
    }

    @Override
    public Operand xorRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] ^ rhs.toIntRaw(rhi));
    }

    @Override
    public Operand slRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] << rhs.toIntRaw(rhi));
    }

    @Override
    public Operand srRaw(final int index, final Operand rhs, final int rhi) {

        return Scalar.fromValue(values[index] >> rhs.toIntRaw(rhi));
    }

    @Override
    public byte peek(int index, int offset) {

        return (byte) (values[index + offset / 4] >> (offset % 4 * 8));
    }

    @Override
    public void poke(int index, int offset, byte value) {

        int bits = values[index + offset / 4];

        bits &= ~(0xFF << (offset % 4 * 8));
        bits |= (value & 0xFF) << (offset % 4 * 8);

        values[index + offset / 4] = bits;

    }

    @Override
    public void assignNeRaw(int index, Operand rhs, int rhi) {
        values[index] = (values[index] != rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignGtRaw(int index, Operand rhs, int rhi) {
        values[index] = (values[index] > rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignLtRaw(int index, Operand rhs, int rhi) {
        values[index] = (values[index] < rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignGtEqRaw(int index, Operand rhs, int rhi) {
        values[index] = (values[index] >= rhs.toIntRaw(rhi)) ? 1 : 0;
    }

    @Override
    public void assignLtEqRaw(int index, Operand rhs, int rhi) {
        values[index] = (values[index] <= rhs.toIntRaw(rhi)) ? 1 : 0;
    }

	//@Override
	public void expandToIndexes() {

		int size=l0;
		if(l1>0) {
			size *= l1;
			if(l2>0) {
				size*=l2;
				if(l3>0) {
					size*=l3;
				}
			}
		}

		final int[] newValues = new int[size];

		System.arraycopy(values, 0, newValues, 0, values.length);

		values = newValues;
	}
}
