/*
 * $Id: HSPError.java,v 1.2 2006/01/13 20:32:10 Yuki Exp $
 */
package hsplet;

/**
 * HSP のエラー値。
 * 
 * @author Yuki
 * @version $Revision: 1.2 $, $Date: 2006/01/13 20:32:10 $
 */
public final class HSPError {

	/** このクラスを含むソースファイルのバージョン文字列。 */
	private static final String fileVersionID = "$Id: HSPError.java,v 1.2 2006/01/13 20:32:10 Yuki Exp $";
	
	/** エラー無し */
	public static final int None = 0;

	/** システムエラーが発生しました */
	public static final int SystemError = 1;

	/** 文法が間違っています */
	public static final int InvalidGrammar = 2;
	
	/** パラメータの値が異常です */
	public static final int InvalidParameterValue = 3;

	/** 計算式でエラーが発生しました */
	public static final int ErrorOnExpression = 4;

	/** パラメータの省略はできません */
	public static final int ParameterCannotBeOmitted = 5;

	/** パラメータの型が違います */
	public static final int ParameterTypeMismatch = 6;

	/** 配列の要素が無効です */
	public static final int IndexOutOfBounds = 7;
	
	/** 有効なラベルが指定されていません */
	public static final int InvalidLabel = 8;
	
	/** サブルーチンやループのネストが深すぎます */
	public static final int SubroutineNestTooDeep = 9;
	
	/** サブルーチン外のreturnは無効です */
	public static final int ReturnOutOfSubroutineDisallowed = 10;

	/** repeat外でのloopは無効です */
	public static final int LoopOutOfRepeatDisallowed = 11;

	/** ファイルが見つからないか無効な名前です */
	public static final int FileNotFound = 12;
	
	/** 画像ファイルがありません */
	public static final int ImageNotFound = 13;

	/** 外部ファイル呼び出し中のエラーです */
	public static final int ErrorOnExecution = 14;

	/** 計算式でカッコの記述が違います */
	public static final int InvalidParenthesis = 15;

	/** パラメータの数が多すぎます */
	public static final int ParameetersTooMany = 16;
	
	/** 文字列式で扱える文字数を越えました */
	public static final int StringTooLong = 17;
	
	/** 代入できない変数名を指定しています */
	public static final int CannotAssign = 18;
	
	/** 0で除算しました */
	public static final int DividedByZero = 19;
	
	/** バッファオーバーフローが発生しました */
	public static final int BufferOverFlow = 20;
	
	/** サポートされない機能を選択しました */
	public static final int UnsupportedOperation = 21;
	
	/** 計算式のカッコが深すぎます */
	public static final int ParenthesisToMany = 22;
	
	/** 変数名が指定されていません */
	public static final int VariableNameNotSpecified = 23;

	/** 整数以外が指定されています */
	public static final int NonIntegerValueSpecified = 24;

	/** 配列の要素書式が間違っています */
	public static final int InvalidFormOfArray = 25;

	/** メモリの確保ができませんでした */
	public static final int OutOfMemory = 26;
	
	/** タイプの初期化に失敗しました */
	public static final int FailToInitializeType = 27;

	/** 関数に引数が設定されていません */
	public static final int ParameterNotSpecified = 28;

	/** スタック領域のオーバーフローです */
	public static final int StackOverFlow = 29;

	/** 無効な名前がパラメーターに指定されています */
	public static final int InvalidNameToParameter = 30;

	/** 異なる型を持つ配列変数に代入しました */
	public static final int AssignToDifferentType = 31;
	
	/** 関数のパラメーター記述が不正です */
	public static final int InvalidFunctionDescription = 32;

	/** オブジェクト数が多すぎます */
	public static final int ObjectsTooMany = 33;

	/** 配列・関数として使用できない型です */
	public static final int TypeCannotUseToArrayOrFunction = 34;

	/** モジュール変数が指定されていません */
	public static final int ModuleVariableNotSpecified = 35;
	
	/** モジュール変数の指定が無効です */
	public static final int InvalidModuleVariableSpecification = 36;

	/** 変数型の変換に失敗しました */
	public static final int FailToConvertVariableType = 37;

	/** 外部DLLの呼び出しに失敗しました */
	public static final int FailToLoadExternalLibrary = 38;

	/** 外部オブジェクトの呼び出しに失敗しました */
	public static final int FailToCallExternalObject = 39;

	/** 関数の戻り値が設定されていません。 */
	public static final int ReturnValueNotSpecified = 40;


}
