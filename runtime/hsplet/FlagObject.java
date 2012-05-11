package hsplet;

import hsplet.variable.Operand;

public class FlagObject {
	public final int newTarget;
	public final Operand returnObject;
	public final boolean returnNow;
	public FlagObject(int i){newTarget=i; returnNow=false; returnObject=null;}
	public FlagObject(Operand O){returnObject=O; returnNow=true; newTarget=0;}
	
	private static final FlagObject emptyFlagObject=new FlagObject(null);
	public static FlagObject getFO(Operand O) {
		if(O==null) return emptyFlagObject;
		return new FlagObject(O);
	}
	public static FlagObject getFO(int i) {
		return new FlagObject(i);
	}
}
