/*
 * $Id: Task.java,v 1.1 2006/01/09 12:07:13 Yuki Exp $
 */
package hsplet;

import java.io.Serializable;

/**
 * button ���荞�݂Ȃǂ̃^�X�N�̃C���^�[�t�F�C�X�B
 * <p>
 * HSPLet 3.0 �� GUI �ƃX�N���v�g�������ʃX���b�h�ōs���邽�߁Abutton �Ȃǂ̃C�x���g�𑦍��Ɏ��s���邱�Ƃ��o���Ȃ��B ���荞�݂����������Ƃ��́A�R���e�L�X�g�ɂ��̃^�X�N��ǉ����Ă����A���̌��
 * await/wait/stop ���Ɏ��s����B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:13 $
 */
public interface Task extends Serializable {

  /** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
  static final String fileVersionID = "$Id: Task.java,v 1.1 2006/01/09 12:07:13 Yuki Exp $";

  /**
   * �^�X�N�����s����B
   * 
   * @param context ���s�R���e�L�X�g�B
   */
  public void run(final Context context);
}
