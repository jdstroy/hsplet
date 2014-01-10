/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.ArrayList;
import java.util.Iterator;

/**
 *
 * @author Kejardon
 */
public class LabelTree {

    private ArrayList<MyTreeThing> allSets = new ArrayList<MyTreeThing>();

    public void addBase(Integer A) {
        for (MyTreeThing set : allSets) {
            if (set.contains(A)) {
                set.used = true;
                return;
            }
        }
        MyTreeThing newSet = new MyTreeThing();
        newSet.add(A);
        newSet.used = true;
        allSets.add(newSet);
    }

    public void remove(Integer A) {
        for (MyTreeThing set : allSets) {
            if (set.remove(A)) {
                if (set.isEmpty()) {
                    allSets.remove(set);
                }
                return;
            }
        }
    }

    public boolean contains(Integer A) {
        for (MyTreeThing set : allSets) {
            if (set.contains(A)) {
                return true;
            }
        }
        return false;
    }
    
    /** Finds the first A that occurs in any MyTreeThing of allSets */

    private MyTreeThing setOf(Integer A) {
        for (MyTreeThing set : allSets) {
            if (set.contains(A)) {
                return set;
            }
        }
        return null;
    }

    public void combine(Integer A, Integer B) {
        MyTreeThing setA = setOf(A);
        MyTreeThing setB = setOf(B);
        boolean foundA = (setA != null);
        boolean foundB = (setB != null);
        if (!foundA) {
            if (foundB) {
                setB.add(A);
                return;
            }
            setA = new MyTreeThing();
            setA.add(A);
            setA.add(B);
            allSets.add(setA);
            return;
        } else if (!foundB) {
            setA.add(B);
            return;
        }
        if (setA == setB) {
            return;
        }
        if (setB.first().intValue() == 0) {
            MyTreeThing tempSet = setA;
            setA = setB;
            setB = tempSet;
        }
        if (setA.used) {
            setA.addAll(setB);
            allSets.remove(setB);
            return;
        }
        setB.addAll(setA);
        allSets.remove(setA);
        return;
    }

    public MyTreeThing[] labelSets() {
        ArrayList<MyTreeThing> usedSets = new ArrayList<MyTreeThing>();
        for (Iterator<MyTreeThing> iter = allSets.iterator(); iter.hasNext();) {
            MyTreeThing nextTree = iter.next();
            if (nextTree.used) {
                usedSets.add(nextTree);
            }
        }
        return usedSets.toArray(new MyTreeThing[0]);
    }
}
