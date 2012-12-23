/*
 * $Id: Array.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $
 */
package hsplet.variable;
import hsplet.HSPError;

/**
 * �z��I�y�����h������킷��{�N���X�B
 * <p>
 * ���̃N���X�͔z��̃C���f�b�N�X�ƃI�t�Z�b�g�̊֌W���`���A �z��̗v�f�̌^�Ɖ��Z�̎����̓T�u�N���X�ɔC����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:12 $
 */
public abstract class Array extends Operand {
    
    public int MAX_DIMENSION_INDEX = 4;

  /** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
  private static final String fileVersionID = "$Id: Array.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $";

  /** �ꎟ���ڂ̗v�f���B */
  protected int l0;

  /** �񎟌��ڂ̗v�f���B */
  protected int l1;

  /** �O�����ڂ̗v�f���B */
  protected int l2;

  /** �l�����ڂ̗v�f���B */
  protected int l3;

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
  public Array(final int l0, final int l1, final int l2, final int l3) {

    this.l0 = l0;
    this.l1 = l1;
    this.l2 = l2;
    this.l3 = l3;

  }

  //@Override
  public int l0() {

    return l0;
  }

  //@Override
  public int l1() {

    return l1;
  }

  //@Override
  public int l2() {

    return l2;
  }

  //@Override
  public int l3() {

    return l3;
  }

  //Size is guaranteed to be > 0. Other than that, nothing's known.
  public void checkIncrementSize(int size) {
    if( (size % l0) == 0 ) {
      if(l1 != 0) {
        error(HSPError.IndexOutOfBounds, ""+size+" "+l0, "Array overflow");
      } else {
        l0++;
        expandToIndexes();
      }
    }
  }
  
  public int checkSize0(int size) {
    if(size>=l0 || size<0){
        error(HSPError.IndexOutOfBounds, ""+size+" "+l0, "Array overflow");
    }
    return size;
  }
  public int checkSize1(int size) {
    if(size>=l1 || size<0){
        error(HSPError.IndexOutOfBounds, ""+size+" "+l1, "Array overflow");
    }
    return size;
  }
  public int checkSize2(int size) {
    if(size>=l2 || size<0){
        error(HSPError.IndexOutOfBounds, ""+size+" "+l2, "Array overflow");
    }
    return size;
  }
  public int checkSize3(int size) {
    if(size>=l3 || size<0){
        error(HSPError.IndexOutOfBounds, ""+size+" "+l3, "Array overflow");
    }
    return size;
  }
  public int checkResize0(int size) {
    boolean expand = (size>=l0);
    if( size<0 ||
       (expand && l1>0) ) {
        error(HSPError.IndexOutOfBounds, ""+size+" "+l1, "Array overflow");
    }
    if(expand) {
      l0=size+1;
      expandToIndexes();
    }
    return size;
  }
  public int checkResize1(int size) {
    boolean expand = (size>=l1);
    if( size<0 ||
       (expand && l2>0) ) {
        error(HSPError.IndexOutOfBounds, ""+size+" "+l2, "Array overflow");
    }
    if(expand) {
      l1=size+1;
      expandToIndexes();
    }
    return size;
  }
  public int checkResize2(int size) {
    boolean expand = (size>=l2);
    if( size<0 ||
       (expand && l3>0) ) {
        error(HSPError.IndexOutOfBounds, ""+size+" "+l3, "Array overflow");
    }
    if(expand) {
      l2=size+1;
      expandToIndexes();
    }
    return size;
  }
  public int checkResize3(int size) {
    boolean expand = (size>=l3);
    if( size<0 ) {
        error(HSPError.IndexOutOfBounds, ""+size, "Array overflow");
    }
    if(expand) {
      l3=size+1;
      expandToIndexes();
    }
    return size;
  }
  //@Override
  public int getIndex(final int i0) {

    if(i0>=l0 || i0<0 ){
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0, "Array overflow");
    }
    return i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1) {

    if(i1>=l1 || i0>=l0 || i1<0 || i0<0 || l1==0){
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1, "Array overflow");
    }
    return i1 * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2) {

    if(i2>=l2 || i1>=l1 || i0>=l0 || i2<0 || i1<0 || i0<0 || l1==0 || l2==0){
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1+" "+i2+" "+l2, "Array overflow");
    }
    return (i2 * l1 + i1) * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2, final int i3) {

    if(i3>=l3 || i2>=l2 || i1>=l1 || i0>=l0 || i3<0 || i2<0 || i1<0 || i0<0 || l1==0 || l2==0 || l3==0){
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1+" "+i2+" "+l2+" "+i3+" "+l3, "Array overflow");
    }
    return ((i3 * l2 + i2) * l1 + i1) * l0 + i0;
  }

  //@Override
  public int getResizeIndex(final int i0) {

    boolean l0Expand = (i0>=l0);
    if( i0<0 ||
       (l0Expand && l1>0) ) {
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+l1, "Array overflow");
    }
    if(l0Expand)
    	expand(i0);
    return i0;
  }

  //@Override
  public int getResizeIndex(final int i0, final int i1) {

    boolean l0Expand = (i0>=l0);
    boolean l1Expand = (i1>=l1);
    if( i1<0 || i0<0 ||
       (l1Expand && l2>0) ||
       (l0Expand && l1>0) ) {
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1+" "+l2, "Array overflow");
    }
    if(l1Expand || l0Expand)
    	expand(i0, i1);
    return i1 * l0 + i0;
  }

  //@Override
  public int getResizeIndex(final int i0, final int i1, final int i2) {

    boolean l0Expand = (i0>=l0);
    boolean l1Expand = (i1>=l1);
    boolean l2Expand = (i2>=l2);
    if( i2<0 || i1<0 || i0<0 ||
       (l2Expand && l3>0) ||
       (l1Expand && l2>0) ||
       (l0Expand && l1>0) ) {
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1+" "+i2+" "+l2+" "+l3, "Array overflow");
    }
    if(l2Expand || l1Expand || l0Expand)
    	expand(i0, i1, i2);
    return (i2 * l1 + i1) * l0 + i0;
  }

  //@Override
  public int getResizeIndex(final int i0, final int i1, final int i2, final int i3) {

    boolean l0Expand = (i0>=l0);
    boolean l1Expand = (i1>=l1);
    boolean l2Expand = (i2>=l2);
    boolean l3Expand = (i3>=l3);
    if( i3<0 || i2<0 || i1<0 || i0<0 ||
       (l2Expand && l3>0) ||
       (l1Expand && l2>0) ||
       (l0Expand && l1>0) ) {
        error(HSPError.IndexOutOfBounds, ""+i0+" "+l0+" "+i1+" "+l1+" "+i2+" "+l2+" "+i3+" "+l3, "Array overflow");
    }
    if(l3Expand || l2Expand || l1Expand || l0Expand)
    	expand(i0, i1, i2, i3);
    return ((i3 * l2 + i2) * l1 + i1) * l0 + i0;
  }



  /**
   * �w�肳�ꂽ�v�f�ɃA�N�Z�X�ł���悤�ɔz��������g������B
   * <p>
   * ���̃��\�b�h�͗v�f����K�؂ɕύX���邾���Ȃ̂ŁB �T�u�N���X�ŃI�[�o�[���C�h����K�v������B
   * </p>
   * 
   * @param index �Œ���m�ۂ������v�f�ԍ��B
   */
  public void expand(final int i0) {
    if(i0 >= l0) l0 = i0+1;
    expandToIndexes();
  }
  public void expand(final int i0, final int i1) {
    if(i0 >= l0) l0 = i0+1;
    if(i1 >= l1) l1 = i1+1;
    expandToIndexes();
  }
  public void expand(final int i0, final int i1, final int i2) {
    if(i0 >= l0) l0 = i0+1;
    if(i1 >= l1) l1 = i1+1;
    if(i2 >= l2) l2 = i2+1;
    expandToIndexes();
  }
  public void expand(final int i0, final int i1, final int i2, final int i3) {
    if(i0 >= l0) l0 = i0+1;
    if(i1 >= l1) l1 = i1+1;
    if(i2 >= l2) l2 = i2+1;
    if(i3 >= l3) l3 = i3+1;
    expandToIndexes();
  }
  public abstract void expandToIndexes();
}
