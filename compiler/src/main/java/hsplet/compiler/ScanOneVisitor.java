package hsplet.compiler;

import java.util.Map;

public class ScanOneVisitor extends NullVisitor {

    private int currentArgCount = 0;
    private final Map<Integer, KLabel> labelsToSet;
    private final int[] labelAddresses;

    public ScanOneVisitor(Map<Integer, KLabel> map, int[] addresses) {
        labelsToSet = map;
        labelAddresses = addresses;
    }

    public void setArgCount(int i) {
        currentArgCount = i;
    }

    public void decArgCount(int i) {
        if (currentArgCount == 0) {
            return;
        }
        if ((--currentArgCount) == 0) {
            KLabel L = labelsToSet.get(labelAddresses[i]);
            L.isMainLabel = true;
            L.isUsed = true;
        }
    }
}