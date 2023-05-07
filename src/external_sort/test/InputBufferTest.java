package external_sort.test;

import static org.junit.Assert.*;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import org.junit.Test;

import external_sort.InputBuffer;
import external_sort.OutputBuffer;

/**
 * This program tests the {@link InputBuffer} class.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 * 
 */
public class InputBufferTest {

	/**
	 * Tests {@link InputBuffer#iterator()}.
	 * 
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	@Test
	public void iterator() throws ClassNotFoundException, IOException {
		iterator(10); // test with 10 numbers
		iterator(20); // test with 20 numbers
		iterator(30); // test with 30 numbers
	}

	/**
	 * Tests {@link InputBuffer#iterator()} using the specified number of elements.
	 * 
	 * @param n
	 *            the number of elements
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	void iterator(int n) throws IOException, ClassNotFoundException {
		OutputBuffer out = new OutputBuffer(1024);
		ObjectOutputStream o = new ObjectOutputStream(out);
		for (Integer i = 0; i < n; i++)
			o.writeObject(i);
		InputBuffer in = new InputBuffer(1024);
		System.arraycopy(out.toByteArray(), 0, in.toByteArray(), 0, out.toByteArray().length);
		Iterator<Object> iterator = in.iterator();
		for (Integer i = 0; i < n; i++) {
			assertEquals(true, iterator.hasNext());
			Object next = iterator.next();
			System.out.print((i == 0 ? "" : ", ") + next);
			assertEquals(i, next); // must get 0, 1, 2, ..., (n-1) from i
		}
		assertEquals(false, iterator.hasNext());
		System.out.println();
		o.close();
	}

}
