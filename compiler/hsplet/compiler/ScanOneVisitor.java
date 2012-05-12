package hsplet.compiler;

import java.util.Map;

public class ScanOneVisitor extends EmptyVisitor {
	private int currentArgCount=0;
	private final Map<Integer, Label> labelsToSet;
	private final int[] labelAddresses;
	public ScanOneVisitor(Map<Integer, Label> map, int[] addresses) {
		labelsToSet=map;
		labelAddresses=addresses;
	}
	public void setArgCount(int i) {
		currentArgCount=i;
	}
	public void decArgCount(int i) {
		if(currentArgCount==0) return;
		if((--currentArgCount)==0) {
			Label L=labelsToSet.get(labelAddresses[i]);
	        L.isMainLabel=true;
	        L.isUsed=true;
		}
	}
}