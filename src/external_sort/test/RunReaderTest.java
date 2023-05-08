package external_sort.test;

import static org.junit.Assert.*;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import external_sort.ExternalSort;
import external_sort.RunReader;
import external_sort.RunWriter;

/**
 * This program tests the {@link RunReader} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class RunReaderTest {

	/**
	 * Tests the {@link RunReader#hasNext()} and {@link RunReader#next()}.
	 */
	@Test
	public void iteration() throws ClassNotFoundException, IOException {
		iteration(100); // test with 100 numbers
		iteration(1000); // test with 1000 numbers
		iteration(10000); // test with 1000 numbers
	}

	/**
	 * Tests the {@link RunReader#hasNext()} and {@link RunReader#next()} using the specified number of elements.
	 * 
	 * @param n
	 *            the number of elements
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	void iteration(int n) throws FileNotFoundException, ClassNotFoundException, IOException {

		int bufferSize = 2048;

		ExternalSort<Integer> externalSort = new ExternalSort<Integer>(null, 2, bufferSize, "test", System.out) {

			@Override
			protected boolean isFull(ArrayList<Integer> list) {
				return false;
			}

		};

		Iterator<Integer> iterator = RunWriterTest.iterator(n);
		String fileName = "test0.run";
		new RunWriter<Integer>(iterator, fileName, bufferSize, externalSort);
		
		RunReader reader = new RunReader(fileName, bufferSize, externalSort);
		for (Integer i = 0; i < n; i++) {
			boolean hasnext = reader.hasNext();
			System.out.println(hasnext);
			assertEquals(true, hasnext);
			Object object = reader.next();
			System.out.println(i);
			System.out.println(object);
			assertEquals(i, object); // must get 0, 1, 2, ..., (n-1) from i
		}
		assertEquals(false, reader.hasNext());
		System.out.println(n + " numbers are retrieved from " + fileName);
	}

}
