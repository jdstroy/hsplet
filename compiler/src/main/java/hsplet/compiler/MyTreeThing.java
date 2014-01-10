/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.ArrayList;
import java.util.TreeSet;

/**
 *
 * @author Kejardon
 */
public class MyTreeThing extends TreeSet<Integer> {

    public boolean used = false;
    private Integer[] mainLabels;

    public Integer[] mainLabels(KLabel[] allLabels) {
        if (mainLabels == null) {
            ArrayList<Integer> mains = new ArrayList<Integer>();
            for (Integer I : this) {
                if (allLabels[I.intValue()].isMainLabel) {
                    mains.add(I);
                }
            }
            mainLabels = mains.toArray(new Integer[0]);
        }
        return mainLabels;
    }
}
