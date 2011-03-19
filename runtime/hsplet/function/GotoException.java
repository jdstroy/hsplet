/*
 * $Id: GotoException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $
 */
package hsplet.function;

/**
 * goto ���������邽�߂̗�O�B
 * <p>
 * HSPLet 3 �ł́AHSP �̂��ׂẴR�[�h����̃��\�b�h���ɋl�ߍ���ł��邽�߁AGOTO ����ɂ̓��[�J���W�����v������K�v������B �ʊ֐�����ړI�̃A�h���X�ɒ��ɔ�ԕ��@�͖������߁A��������
 * �������ꂽ���C���֐��ɖ߂�����A�ړI�̃A�h���X�ɃW�����v����B
 * </p>
 * <p>
 * ���̗�O�𓊂���ƁA�������ꂽ���C���֐����œK�؂Ƀn���h������A�ړI�̃��x���ɃW�����v���邱�Ƃ��o����B
 * </p>
 * <p>
 * �ʏ킱�̗�O�𒼐ڎg�p���邱�Ƃ͖����A�܂��g�p���ׂ��ł͂Ȃ��B ����� {@link hsplet.function.ProgramCommand#goto_(hsplet.Context, int, boolean) }
 * ���g�p����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:12 $
 */
public class GotoException extends RuntimeException {

  /** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
  private static final String fileVersionID = "$Id: GotoException.java,v 1.1 2006/01/09 12:07:12 Yuki Exp $";

  /** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
  private static final long serialVersionUID = 7274255688514910944L;

  /** �W�����v�惉�x���B */
  public final int label;

  /**
   * �W�����v�惉�x�����w�肵�ăI�u�W�F�N�g���\�z����B
   * 
   * @param label �W�����v�惉�x���B
   */
  public GotoException(int label) {

    this.label = label;
  }

}
