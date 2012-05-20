/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

/**
 *
 * @author Kejardon
 */
public class Label extends org.objectweb.asm.Label {

    public int branchesToHere = 0;
    public int currentCount = 0;
    public int myIndex = -1;
    public boolean isMainLabel = false;
    public boolean isUsed = false;
    public Object extra;
    /*public Set<Label> extra_labels;
    public List<Integer> extra_integers;

    public Set<Label> extra_labels() {
        if (extra_labels == null) {
            extra_labels = new SparseHashSet<Label>();
        }
        return extra_labels;
    }

    public List<Integer> extra_integers() {
        if (extra_integers == null) {
            extra_integers = new SparseArrayList<Integer>();
        }
        return extra_integers;
    }*/

    public void relyOn(Label other) {
        if (isUsed) {
            return;
        }

        if (other == this) {
            return;
        }

        if (other.isUsed) {
            isUsed = true;
            extra = null;
        } else if (extra == null) {
            extra = other;
        } else if (extra instanceof Label) {
            Label old = (Label) extra;
            HashSet<Label> newExtra = new HashSet<Label>();
            extra = newExtra;
            newExtra.add(old);
            newExtra.add(other);
        } else if (extra instanceof HashSet) {
            ((HashSet<Label>) extra).add(other);
        } else {
            throw new RuntimeException("Error in label logic! Unknown thing in extra in second scan.");
        }
    }
}


