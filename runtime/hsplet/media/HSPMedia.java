/*
 * $Id: HSPMedia.java,v 1.4 2006/01/21 12:48:17 Yuki Exp $
 */
package hsplet.media;

import java.io.Serializable;

/**
 * ���f�B�A�I�u�W�F�N�g�����ʂŎ��C���^�[�t�F�C�X�B
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:17 $
 */
public interface HSPMedia extends Serializable {

	/** ���̃N���X���܂ރ\�[�X�t�@�C���̃o�[�W����������B */
	static final String fileVersionID = "$Id: HSPMedia.java,v 1.4 2006/01/21 12:48:17 Yuki Exp $";

	/**
	 * �Đ����J�n����B
	 * <p>���łɓr���܂ōĐ�����Ă����Ƃ��́A��������Đ����J�n����B
	 * </p>
	 */
	public void play();

	/**
	 * �Đ����~����B
	 */
	public void stop();

	/**
	 * �Đ��ʒu���~���b�P�ʂŐݒ肷��B
	 * @param value �Đ��ʒu�B
	 */
	public void setPosition(int value);

	/**
	 * �Đ��ʒu���~���b�P�ʂŎ擾����B
	 * @return �Đ��ʒu�B
	 */
	public int getPosition();
	
	/**
	 * �Đ������ǂ����擾����B
	 * @return �Đ������ǂ����B
	 */
	public boolean isPlaying();

	/**
	 * ���ׂẴ��\�[�X��j������B
	 */
	public void dispose();
}
