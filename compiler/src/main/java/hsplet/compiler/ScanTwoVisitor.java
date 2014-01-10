package hsplet.compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanTwoVisitor extends NullVisitor {
	//public KLabel currentLabel=null;
	public LabelTree labelTree=new LabelTree();
	
	public int currentStatementAddress=0;	//input for this class
	//public HashMap<Integer, KLabel> foundExceptions=new HashMap<Integer, KLabel>();	//Output to compiler
	private ArrayList<LoopContainer> loopLabels=new ArrayList<LoopContainer>();
	public void addLoopLabel(KLabel L, KLabel E) {
		L.myIndex=currentStatementAddress;
		loopLabels.add(new LoopContainer(L, E));
	}
	public void loopReliesOn(KLabel L, KLabel E) {
		//for(LoopContainer cont : loopLabels) {	//A reverse search will be more efficient most of the time
		for(int i=loopLabels.size()-1;i>=0;i--) {
			LoopContainer cont=loopLabels.get(i);
			if(cont.loop!=L) continue;
			if(cont.internalLabels==null)
				cont.internalLabels=new ArrayList<KLabel>();
			//else if(cont.internalLabels.contains(E)) return;	//Not possible to happen.
			cont.internalLabels.add(E);
			return;
		}
	}
	public HashMap<Integer, KLabel> getLoopExceptions() {
		HashMap<Integer, KLabel> results=new HashMap<Integer, KLabel>();
		for(LoopContainer cont : loopLabels) {
			if((cont.entrance!=null)&&(cont.entrance.isUsed)) continue;	//Will be detected as used naturally
			if(cont.internalLabels==null) continue;	//Is not used, will be skipped
			for(KLabel label : cont.internalLabels) {
				if(label.isUsed) {
					results.put(cont.loop.myIndex, label);
					continue;
				}
			}
		}
		return results;
	}
	private class LoopContainer {
		KLabel loop;
		KLabel entrance;
		ArrayList<KLabel> internalLabels;
		public LoopContainer(KLabel L, KLabel E){loop=L; entrance=E;}
	}
}
