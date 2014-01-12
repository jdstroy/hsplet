/*
 * $Id: RunException.java,v 1.1 2006/01/16 19:34:23 Yuki Exp $
 */
package hsplet.function;

/**
 * run を実現するための例外。
 * <p>
 * この例外を投げるとスレッドの最上位でキャッチされ、新しいクラスが実行される。
 * </p>
 * <p>
 * 通常この例外を直接使用することは無く、また使用すべきではない。 代わりに {@link hsplet.function.ProgramCommand#run(hsplet.Context, String, String) }
 * を使用する。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/16 19:34:23 $
 */
public class RunException extends RuntimeException {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: RunException.java,v 1.1 2006/01/16 19:34:23 Yuki Exp $";

	/** 直列化復元時に、データの互換性を確認するためのバージョン番号。 */
	private static final long serialVersionUID = 2931064258661525139L;

	private final Class runClass;

	private final String cmdline;

	/**
	 * オブジェクトを構築する。
	 * @param runClass 起動するクラス。
	 * @param cmdline コマンドライン引数。
	 */
	public RunException(final Class runClass, final String cmdline) {
		this.runClass = runClass;
		this.cmdline = cmdline;
	}

	/**
	 * コマンドライン引数を取得する。
	 * @return コマンドライン引数。
	 */
	public String getCmdline() {
		return cmdline;
	}

	/**
	 * 起動するクラスを取得する。
	 * @return 起動するクラス。
	 */
	public Class getRunClass() {
		return runClass;
	}

}
