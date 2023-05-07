package external_sort.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import external_sort.ExternalSort;
import external_sort.OptimizedExternalSort;

/**
 * This program tests the {@link ExternalSort} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class ExternalSortTest {

	/**
	 * Tests the {@link ExternalSort} class.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	@Test
	public void iteration() throws ClassNotFoundException, IOException {
		sort(10, 10, 2, 1024, false, System.out); // test with 10 numbers with degree 2
		sort(40, 10, 2, 1024, false, System.out); // test with 40 numbers with degree 2
		sort(160, 10, 2, 1024, false, System.out); // test with 160 numbers with degree 2
		sort(1600, 100, 2, 1024, false, System.out); // test with 1600 numbers with degree 2
		sort(16000, 1000, 2, 1024, false, System.out); // test with 16000 numbers with degree 2
		sort(160000, 10000, 2, 1024, false, System.out); // test with 160000 numbers with degree 2
		sort(160000, 10000, 4, 1024, false, System.out); // test with 160000 numbers with degree 4
		sort(160000, 10000, 16, 1024, false, System.out); // test with 160000 numbers with degree 16
	}

	/**
	 * Tests the {@link ExternalSort} class.
	 * 
	 * @param n
	 *            the number of elements
	 * @param m
	 *            the number of elements per initial run
	 * @param degree
	 *            the degree of external sort
	 * @param bufferSize
	 *            the buffer size
	 * @param optimized
	 *            a flag indicating whether to use {@code OptimizedExternalSort} or {@code ExternalSort}
	 * @param out
	 *            a {@code PrintStream}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	static void sort(int n, int m, int degree, int bufferSize, boolean optimized, PrintStream out)
			throws FileNotFoundException, ClassNotFoundException, IOException {

		Iterator<Integer> iterator = new Iterator<Integer>() { // iterator over (n-1), (n-2), ..., 2, 1, 0

			int i = n - 1;

			@Override
			public boolean hasNext() {
				return i >= 0;
			}

			@Override
			public Integer next() {
				return i--;
			}

		};
		sort(n, m, degree, bufferSize, optimized, out, iterator, true);
	}

	/**
	 * Tests the {@link ExternalSort} class.
	 * 
	 * @param n
	 *            the number of elements
	 * @param m
	 *            the number of elements per initial run
	 * @param degree
	 *            the degree of external sort
	 * @param bufferSize
	 *            the buffer size
	 * @param optimized
	 *            a flag indicating whether to use {@code OptimizedExternalSort} or {@code ExternalSort}
	 * @param out
	 *            a {@code PrintStream}
	 * @param iterator
	 *            an {@code Iterator} that provides the input data
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	static <T extends Comparable<? super T>> void sort(int n, int m, int degree, int bufferSize, boolean optimized,
			PrintStream out, Iterator<T> iterator, boolean consecutiveIntegers)
			throws ClassNotFoundException, IOException {
		out.println("degree: " + degree);
		out.println("bufferSize: " + bufferSize);

		long startTime = System.nanoTime();

		ExternalSort<T> e = optimized ? new OptimizedExternalSort<T>(iterator, degree, bufferSize, "test", out) {

			@Override
			protected boolean isFull(ArrayList<T> list) {
				return list.size() >= m;
			}

		} : new ExternalSort<T>(iterator, degree, bufferSize, "test", out) {

			@Override
			protected boolean isFull(ArrayList<T> list) {
				return list.size() >= m;
			}

		};

		long time = System.nanoTime();
		for (Integer i = 0; i < n; i++) {
			assertEquals(true, e.hasNext());
			T next = e.next();
			if (consecutiveIntegers)
				assertEquals(i, next); // must get 0, 1, 2, ..., (n-1) from e
		}
		assertEquals(false, e.hasNext());
		long totalTime = System.nanoTime() - startTime;
		out.println(
				"retrieved " + n + " elements" + String.format(" (%.5f seconds)", (System.nanoTime() - time) * 1.0e-9));
		out.println("number of buffer reads: " + e.bufferReadCount());
		out.println("number of buffer writes: " + e.bufferWriteCount());
		out.println("bytes read: " + e.bytesRead());
		out.println("bytes written: " + e.bytesWritten());
		out.println("elapsed time: " + String.format(" %.5f seconds", totalTime * 1.0e-9));
		out.println();
	}

}
