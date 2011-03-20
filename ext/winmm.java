/*
 * $Id: winmm.java,v 1.1 2006/01/09 12:07:06 Yuki Exp $
 */

import hsplet.function.FunctionBase;

/**
 * winmm.dll の関数群を実装するクラス。
 * <p>
 * このクラスは DLL の実装が如何に容易であるかをよくあらわしている。
 * </p>
 * <p>
 * すなはち、DLL 名を付けたクラスをデフォルトパッケージに配置し、 public static なメソッドを実装するだけでよい。
 * </p>
 * <p>
 * 戻り値には void/int/double/String/ByteString/Operand が使用できる。
 * </p>
 * <p>
 * 引数には int/double/string/ByteString/Operand/Context を受け取ることが出来る。
 * </p>
 * <p>
 * Operand を受け取るときは、必ず次の引数を int にし、配列のインデックスを受け取らなければいけない。
 * </p>
 * 
 * @author Yuki
 * @version $Revision: 1.1 $, $Date: 2006/01/09 12:07:06 $
 */
public class winmm extends FunctionBase {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: winmm.java,v 1.1 2006/01/09 12:07:06 Yuki Exp $";

	private static final long startMillis = System.currentTimeMillis();

	/**
	 * 起動してから現在までのミリ秒を取得する。
	 * 
	 * @return 起動してから現在までのミリ秒。
	 */
	public static int timeGetTime() {

		return (int) (System.currentTimeMillis() - startMillis);
	}


        public static int timeBeginPeriod(int i) {
            throw new UnsupportedOperationException("Not yet implemented.");
        }


        public static int timeEndPeriod(int i) {
            throw new UnsupportedOperationException("Not yet implemented.");
        }
}
