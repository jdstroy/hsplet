package QuickSort.Comparator;

import hsplet.variable.ByteString;
import QuickSort.Container.*;

public class ByteStringComparator implements java.util.Comparator<ByteStringContainer> {
	public int compare(final ByteStringContainer o1, final ByteStringContainer o2) {
		ByteString
			bs1 = (o1).value,
			bs2 = (o2).value;
		int sub = bs1.compareSub(0, bs2);

		if (sub != 0) {
			return sub;
		} else {
			return bs1.length()>bs2.length() ? 1 : 0;
		}
	}
}