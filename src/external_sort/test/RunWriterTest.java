package external_sort.test;

import static org.junit.Assert.*;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import org.junit.Test;

import external_sort.ExternalSort;
import external_sort.InputBuffer;
import external_sort.RunWriter;

/**
 * This program tests the {@link RunWriter} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class RunWriterTest {

	/**
	 * Tests the constructor of {@link RunWriter}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	@Test
	public void constructor() throws ClassNotFoundException, IOException {
		constructor(100, 1024); // test with 100 numbers
		constructor(1000, 1024); // test with 1000 numbers
		constructor(10000, 1024); // test with 1000 numbers
		constructor(100000, 1024); // test with 10000 numbers
		constructor(1000000, 1024); // test with 10000 numbers
		constructor(10000000, 32 * 1024 * 1024); // test with 10000 numbers
		constructor(10000000, 4 * 1024 * 1024); // test with 10000 numbers
		constructor(10000000, 512 * 1024); // test with 10000 numbers
		constructor(10000000, 64 * 1024); // test with 10000 numbers
		constructor(10000000, 8 * 1024); // test with 10000 numbers
		constructor(10000000, 1 * 1024); // test with 10000 numbers
	}

	/**
	 * Tests the constructor of {@link RunWriter} using the specified number of elements.
	 * 
	 * @param n
	 *            the number of elements
	 * @param bufferSize
	 *            the buffer size
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	void constructor(int n, int bufferSize) throws FileNotFoundException, ClassNotFoundException, IOException {

		ExternalSort<Integer> externalSort = new ExternalSort<Integer>(null, 2, bufferSize, "test", System.out) {

			@Override
			protected boolean isFull(ArrayList<Integer> list) {
				return false;
			}

		};

		Iterator<Integer> iterator = iterator(n);

		String fileName = "test0.run";
		long time = System.nanoTime();
		new RunWriter<Integer>(iterator, fileName, bufferSize, externalSort);
		File file = new File(fileName);
		FileInputStream in = new FileInputStream(fileName);
		int count = 0;
		InputBuffer b = new InputBuffer(bufferSize);
		for (long i = 0; i < file.length(); i += bufferSize) {
			in.read(b.toByteArray());
			Iterator<Object> iterator2 = b.iterator();
			while (iterator2.hasNext()) {
				assertEquals(count++, iterator2.next()); // must get 0, 1, 2, ..., (n-1)
			}
		}
		assertEquals(n, count);
		System.out.println(n + " numbers are retrieved from " + fileName + " (" + file.length() + " bytes, "
				+ String.format(" %.5f seconds", (System.nanoTime() - time) * 1.0e-9) + ")");
		in.close();
	}

	/**
	 * Returns an {@code Iterator} that iterates over {@code n} consecutive integers from 0.
	 * 
	 * @param n
	 *            the number of integers
	 * @return an {@code Iterator} that iterates over {@code n} consecutive integers from 0
	 */
	public static Iterator<Integer> iterator(int n) {
		return new Iterator<Integer>() { // iterator over 0, 1, 2, ..., (n-1)

			int i = 0;

			@Override
			public boolean hasNext() {
				return i < n;
			}

			@Override
			public Integer next() {
				return i++;
			}

		};
	}

}
