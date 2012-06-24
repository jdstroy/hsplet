package QuickSort.Comparator;

import QuickSort.Container.*;

public class IntComparator implements java.util.Comparator<Container> {
	public int compare(final Container o1, final Container o2) {
		return ((IntContainer)o1).value - ((IntContainer)o2).value;
	}
}