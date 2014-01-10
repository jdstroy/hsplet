/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package hsplet.compiler.codescanner;

import java.util.Collection;
import java.util.SortedMap;

/**
 *
 * @author jdstroy
 */
public interface ICodeScanningVisitorResult {
    public void incrementFieldUsage(FieldDescriptor fd);
    public SortedMap<? extends Number, ? extends Collection<FieldDescriptor>> getUsages();
    public int getMaxLocals();
    public void checkMaximumLocal(int localSlot);
}
