/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler;

import java.util.HashSet;

/**
 * An extended Label with extra stuff.  We should probably make a container 
 * class for this instead.
 * @author Kejardon
 */
public class KLabel extends org.objectweb.asm.Label {

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

    public void relyOn(KLabel other) {
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
        } else if (extra instanceof KLabel) {
            KLabel old = (KLabel) extra;
            HashSet<KLabel> newExtra = new HashSet<KLabel>();
            extra = newExtra;
            newExtra.add(old);
            newExtra.add(other);
        } else if (extra instanceof HashSet) {
            ((HashSet<KLabel>) extra).add(other);
        } else {
            throw new RuntimeException("Error in label logic! Unknown thing in extra in second scan.");
        }
    }
}


