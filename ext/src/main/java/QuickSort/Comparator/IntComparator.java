package QuickSort.Comparator;

import QuickSort.Container.*;

public class IntComparator implements java.util.Comparator<IntContainer> {
	public int compare(final IntContainer o1, final IntContainer o2) {
		return (o1).value - (o2).value;
	}
}