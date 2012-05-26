/*
 * $Id: Array.java,v 1.2 2006/01/13 20:32:12 Yuki Exp $
 */
package hsplet.variable;

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
  
  //@Override
  public int getIndex(final int i0, final int i1) {

    if(i1>l1){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l1 exceeded: "+i1+" "+l1);
    }
    return i1 * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2) {

    if(i2>l2){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l2 exceeded: "+i2+" "+l2);
    }
    return (i2 * l1 + i1) * l0 + i0;
  }

  //@Override
  public int getIndex(final int i0, final int i1, final int i2, final int i3) {

    if(i3>l3){
	    if(Variable.class.isAssignableFrom(this.getClass())) System.out.print("v");
	    System.out.println(errorIndex()+" l3 exceeded: "+i3+" "+l3);
    }
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
  public void expand(final int index) {

    // �v�f���� 1 ����Ȃ��Ō�̎������g������̂��Ó��Ǝv����B �v�f�̍Ĕz�u���s�v�Ȃ̂����ꂵ���B
    // 10, 2, 1, 1 -> 10, 3, 1, 1

    int expandSize = (index + 1) - l0 * l1 * l2 * l3;

    if (l3 != 1) {
      l3 += (expandSize + 1) / (l0 * l1 * l2);
    } else if (l2 != 1) {
      l2 += (expandSize + 1) / (l0 * l1);
    } else if (l1 != 1) {
      l1 += (expandSize + 1) / (l0);
    } else {
      l0 += expandSize + 1;
    }

  }
}
