package external_sort.test;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.Iterator;

import org.junit.Test;

import external_sort.ExternalSort;

/**
 * This program tests the {@link ExternalSort} class while changing the configuration parameters.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class ExternalSortConfigurationTest {

	/**
	 * Tests the {@link ExternalSort} class while changing the configuration parameters.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	@Test
	public void iteration() throws ClassNotFoundException, IOException {
		int n = 10000000; // the number of elements
		int m = 2000; // the number of elements per initial run
		sort(n, m, 8 - 1, 64 * 1024, false, System.out); // test with n numbers with degree 7 and 1M memory
		System.gc();
		sort(n, m, 64 - 1, 8 * 1024, false, System.out); // test with n numbers with degree 63 and 1M memory
		System.gc();
		sort(n, m, 512 - 1, 1 * 1024, false, System.out); // test with n numbers with degree 511 and 1M memory
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

		Iterator<Double> iterator = new Iterator<Double>() { // iterator over (n-1), (n-2), ..., 2, 1, 0

			int count = 0;

			@Override
			public boolean hasNext() {
				return count < n;
			}

			@Override
			public Double next() {
				count++;
				return Math.random();
			}

		};
		ExternalSortTest.sort(n, m, degree, bufferSize, optimized, out, iterator, false);
	}
}
