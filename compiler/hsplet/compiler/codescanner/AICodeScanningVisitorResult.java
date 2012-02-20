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
import java.util.concurrent.atomic.AtomicInteger;

/**
 *
 * @author jdstroy
 */
public class AICodeScanningVisitorResult implements ICodeScanningVisitorResult {

    public AICodeScanningVisitorResult(MethodDescriptor methodDescriptor) {
        this.methodDescriptor = methodDescriptor;
    }
    
    public MethodDescriptor methodDescriptor;
    public Map<FieldDescriptor, AtomicInteger> fieldUsage = new HashMap<FieldDescriptor, AtomicInteger>();
    public int maxLocals = -1;
    
    @Override
    public void incrementFieldUsage(FieldDescriptor fd) {
        if (fieldUsage.containsKey(fd)) {
            fieldUsage.get(fd).incrementAndGet();
        } else {
            fieldUsage.put(fd, new AtomicInteger(1));
        }
    }

    @Override
    public SortedMap<? extends Number, ? extends Collection<FieldDescriptor>> getUsages() {
        SortedMap<? extends Number, ? extends Collection<FieldDescriptor>> retVal;
        
        TreeMap<AtomicInteger, ArrayList<FieldDescriptor>> map = new TreeMap<AtomicInteger, ArrayList<FieldDescriptor>>();
        retVal = map;
        Set<FieldDescriptor> keys = fieldUsage.keySet();
        for(FieldDescriptor fd : keys) {
            AtomicInteger uses = fieldUsage.get(fd);
            if (map.containsKey(uses)) {
                map.get(uses).add(fd);
            } else {
                ArrayList<FieldDescriptor> fdList = new ArrayList<FieldDescriptor>();
                fdList.add(fd);
                map.put(uses, fdList);
            }
        }
        
        return retVal;
    }

    @Override
    public void checkMaximumLocal(int localSlot) {
        if (localSlot > maxLocals) {
            maxLocals = localSlot;
        }
    }

    @Override
    public int getMaxLocals() {
        return maxLocals;
    }
}
