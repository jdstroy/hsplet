/*
 * $Id: HSPMedia.java,v 1.4 2006/01/21 12:48:17 Yuki Exp $
 */
package hsplet.media;

import java.io.Serializable;

/**
 * メディアオブジェクトが共通で持つインターフェイス。
 * 
 * @author Yuki
 * @version $Revision: 1.4 $, $Date: 2006/01/21 12:48:17 $
 */
public interface HSPMedia extends Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	static final String fileVersionID = "$Id: HSPMedia.java,v 1.4 2006/01/21 12:48:17 Yuki Exp $";

	/**
	 * 再生を開始する。
	 * <p>すでに途中まで再生されていたときは、続きから再生を開始する。
	 * </p>
	 */
	public void play();

	/**
	 * 再生を停止する。
	 */
	public void stop();

	/**
	 * 再生位置をミリ秒単位で設定する。
	 * @param value 再生位置。
	 */
	public void setPosition(int value);

	/**
	 * 再生位置をミリ秒単位で取得する。
	 * @return 再生位置。
	 */
	public int getPosition();
	
	/**
	 * 再生中かどうか取得する。
	 * @return 再生中かどうか。
	 */
	public boolean isPlaying();

	/**
	 * すべてのリソースを破棄する。
	 */
	public void dispose();
}
