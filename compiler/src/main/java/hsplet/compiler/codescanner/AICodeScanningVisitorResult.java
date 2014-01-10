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
 * AtomicInteger variation of CodeScanningVisitorResult.  Worth benchmarking 
 * this versus the regular Integer variation.
 * AtomicInteger, unlike Integer, is mutable.  Depending on how the allocations
 * and mutations are performed, AtomicInteger might be faster.  One could 
 * implement a variation of CodeScanningVisitorResult through an Integer-like 
 * class that is mutable without the overhead of the hardware-level atomicity 
 * that this class provides, as we're only using this in a single thread.
 * 
 * Not that an int[] array is insufficient for our purposes here, as it would 
 * not be able to implement the Comparable&lt;T&gt; interface required in a 
 * TreeMap&lt;&lt;T&gt;, ArrayList&lt;FieldDescriptor&gt;&gt;
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
        
        TreeMap<AtomicInteger, ArrayList<FieldDescriptor>> map = new TreeMap<AtomicInteger, ArrayList<FieldDescriptor>>();
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
        
        return map;
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
