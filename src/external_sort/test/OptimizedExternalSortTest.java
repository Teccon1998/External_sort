package external_sort.test;

import java.io.IOException;

import org.junit.Test;

/**
 * This program tests the {@code OptimizedExternalSort} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class OptimizedExternalSortTest {

	/**
	 * Tests the {@code OptimizedExternalSort} class.
	 */
	@Test
	public void iteration() throws ClassNotFoundException, IOException {
		ExternalSortTest.sort(10000000, 2000000, 2, 4 * 1024, false, System.out); // test with 10000000 numbers
		ExternalSortTest.sort(10000000, 2000000, 2, 4 * 1024, true, System.out); // test with 10000000 numbers

		ExternalSortTest.sort(10000000, 1000000, 3, 4 * 1024, false, System.out); // test with 10000000 numbers
		ExternalSortTest.sort(10000000, 1000000, 3, 4 * 1024, true, System.out); // test with 10000000 numbers
	}

}
