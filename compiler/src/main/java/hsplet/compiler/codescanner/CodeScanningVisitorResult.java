/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.codescanner;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.SortedMap;
import java.util.TreeMap;

/**
 *
 * @author jdstroy
 */
public class CodeScanningVisitorResult implements ICodeScanningVisitorResult {

    public CodeScanningVisitorResult(MethodDescriptor methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }
    public MethodDescriptor methodDescriptor;
    public Map<FieldDescriptor, Integer> fieldUsage = new HashMap<FieldDescriptor, Integer>();
    public int maxLocals = -1;

    @Override
    public void incrementFieldUsage(FieldDescriptor fd) {
        if (fieldUsage.containsKey(fd)) {
            fieldUsage.put(fd, new Integer(fieldUsage.get(fd).intValue() + 1));
        } else {
            fieldUsage.put(fd, new Integer(1));
        }
    }

    @Override
    public SortedMap<? extends Number, ? extends Collection<FieldDescriptor>> getUsages() {
        TreeMap<Integer, ArrayList<FieldDescriptor>> map = new TreeMap<Integer, ArrayList<FieldDescriptor>>();
        Set<FieldDescriptor> keys = fieldUsage.keySet();
        for (FieldDescriptor fd : keys) {
            Integer uses = fieldUsage.get(fd);
            if (map.containsKey(uses)) {
                map.get(uses).add(fd);
            } else {
                ArrayList<FieldDescriptor> fdList = new ArrayList<FieldDescriptor>();
                fdList.add(fd);
                map.put(uses, fdList);
            }
        }

        return map;
    }

    @Override
    public int getMaxLocals() {
        return maxLocals;
    }

    @Override
    public void checkMaximumLocal(int localSlot) {
        if (localSlot > maxLocals) {
            maxLocals = localSlot;
        }
    }
}
