/*
 * $Id: EmptyVisitor.java,v 1.1 2006/01/09 12:07:05 Yuki Exp $
 */
package hsplet.compiler;

import java.io.Serializable;


/**
 * �������s���Ȃ� MethodVisitor�B
 * <p>
 * ASM �̒ǉ����C�u�����ɂ������N���X�͂��邪�T�C�Y���傫���̂ŁE�E�E�B
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:05 $
 */
public class EmptyVisitor extends NullVisitor implements  Serializable {

  /** ���񉻕������ɁA�f�[�^�̌݊������m�F���邽�߂̃o�[�W�����ԍ��B */
  private static final long serialVersionUID = -3781394417631917269L;

  /** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
  private static final String fileVersionID = "$Id: EmptyVisitor.java,v 1.1 2006/01/09 12:07:05 Yuki Exp $";
  
  public static final EmptyVisitor mv=new EmptyVisitor();

}
