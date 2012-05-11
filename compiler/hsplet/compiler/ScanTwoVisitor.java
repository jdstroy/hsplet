package hsplet.compiler;

import java.util.ArrayList;
import java.util.HashMap;

public class ScanTwoVisitor extends EmptyVisitor {
	//public Label currentLabel=null;
	public LabelTree labelTree=new LabelTree();
	
	public int currentStatementAddress=0;	//input for this class
	//public HashMap<Integer, Label> foundExceptions=new HashMap<Integer, Label>();	//Output to compiler
	private ArrayList<LoopContainer> loopLabels=new ArrayList<LoopContainer>();
	public void addLoopLabel(Label L, Label E) {
		L.myIndex=currentStatementAddress;
		loopLabels.add(new LoopContainer(L, E));
	}
	public void loopReliesOn(Label L, Label E) {
		//for(LoopContainer cont : loopLabels) {	//A reverse search will be more efficient most of the time
		for(int i=loopLabels.size()-1;i>=0;i--) {
			LoopContainer cont=loopLabels.get(i);
			if(cont.loop!=L) continue;
			if(cont.internalLabels==null)
				cont.internalLabels=new ArrayList<Label>();
			//else if(cont.internalLabels.contains(E)) return;	//Not possible to happen.
			cont.internalLabels.add(E);
			return;
		}
	}
	public HashMap<Integer, Label> getLoopExceptions() {
		HashMap<Integer, Label> results=new HashMap<Integer, Label>();
		for(LoopContainer cont : loopLabels) {
			if((cont.entrance!=null)&&(cont.entrance.isUsed)) continue;	//Will be detected as used naturally
			if(cont.internalLabels==null) continue;	//Is not used, will be skipped
			for(Label label : cont.internalLabels) {
				if(label.isUsed) {
					results.put(cont.loop.myIndex, label);
					continue;
				}
			}
		}
		return results;
	}
	private class LoopContainer {
		Label loop;
		Label entrance;
		ArrayList<Label> internalLabels;
		public LoopContainer(Label L, Label E){loop=L; entrance=E;}
	}
}
