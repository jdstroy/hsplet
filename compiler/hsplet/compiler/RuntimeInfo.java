/*
 * $Id: RuntimeInfo.java,v 1.2 2006/01/13 05:20:55 Yuki Exp $
 */
package hsplet.compiler;

import hsplet.compiler.ByteCode.Code;

import java.io.Serializable;
import java.lang.reflect.Method;

/**
 * ランタイムに関する情報（バイトコードとメソッドの関連付け）を提供するインターフェイス。
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 05:20:55 $
 */
public interface RuntimeInfo extends Serializable {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	static final String fileVersionID = "$Id: RuntimeInfo.java,v 1.2 2006/01/13 05:20:55 Yuki Exp $";

	/**
	 * コードを実行するクラスを取得する。
	 * @param ax バイトコード。
	 * @param code 命令コード。
	 * @return 実行するクラス。
	 */
	public Class getClassFor(final ByteCode ax, final Code code);

	/**
	 * コードを実行するメソッドを取得する。
	 * <p>このメソッドの宣言されているクラスが、実際に命令を実行する保証は無い。
	 * 	実際に命令を実行するクラスは {@link RuntimeInfo#getClassFor(ByteCode,Code)} で取得する。
	 * </p>
	 * @param ax バイトコード。
	 * @param code 命令コード。
	 * @return 実行するメソッド。
	 */
	public Method getMethodFor(final ByteCode ax, final ByteCode.Code code);

	//Compiler provides the name, runtimeinfo gets the actual method
	public Method getMethodFor(final ByteCode ax, final ByteCode.Code code, final String name);
}
