package QuickSort.Comparator;

import QuickSort.Container.*;

public class DoubleComparator implements java.util.Comparator<DoubleContainer> {
	public int compare(final DoubleContainer o1, final DoubleContainer o2) {
		double sub = (o1).value - (o2).value;
		if (sub == 0) return 0;
		return sub > 0 ? 1 : -1;
	}
}