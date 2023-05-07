package external_sort.test;

import static org.junit.Assert.*;

import java.util.Arrays;
import java.util.Iterator;

import org.junit.Test;

import external_sort.OrderedMergeIterator;

/**
 * This program tests the {@link OrderedMergeIterator} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class OrderedMergeIteratorTest {

	/**
	 * Tests {@link OrderedMergeIterator#next()}.
	 */
	@Test
	public void next() {
		next(10); // test with 10 numbers
		next(20); // test with 20 numbers
		next(30); // test with 30 numbers
	}

	/**
	 * Tests {@link OrderedMergeIterator#next()} using the specified number of elements.
	 * 
	 * @param n
	 *            the number of elements
	 */
	void next(int n) {

		Iterator<Integer> iEven = new Iterator<Integer>() { // iterator over even numbers

			int i = 0;

			@Override
			public boolean hasNext() {
				return i < n;
			}

			@Override
			public Integer next() {
				i += 2;
				return i - 2;
			}

		};

		Iterator<Integer> iOdd = new Iterator<Integer>() { // iterator over odd numbers

			int i = 1;

			@Override
			public boolean hasNext() {
				return i < n;
			}

			@Override
			public Integer next() {
				i += 2;
				return i - 2;
			}

		};
		OrderedMergeIterator<Integer> i = new OrderedMergeIterator<Integer>(Arrays.asList(iEven, iOdd));
		for (Integer e = 0; e < n; e++) {
			assertEquals(true, i.hasNext());
			Integer next = i.next();
			System.out.print((e == 0 ? "" : ", ") + next);
			assertEquals(e, next); // must get 0, 1, 2, ..., (n-1) from i
		}
		assertEquals(false, i.hasNext());
		System.out.println();
	}

}
