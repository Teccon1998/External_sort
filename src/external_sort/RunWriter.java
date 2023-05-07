package external_sort;

import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.util.Iterator;

import external_sort.OutputBuffer.BufferOverflowException;

/**
 * A {@code RunWriter} writes objects on a run using an {@code OutputBuffer}.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 *
 * @param <T>
 *            the type of the objects
 */
public class RunWriter<T extends Comparable<? super T>> {

	/**
	 * The last object obtained from the input {@code Iterator}.
	 */
	T last = null;

	/**
	 * Constructs a {@code RunWriter}.
	 * 
	 * @param iterator
	 *            the input {@code Iterator}
	 * @param fileName
	 *            the name of the file (i.e., the run)
	 * @param bufferSize
	 *            the output buffer size
	 * @param externalSort
	 *            the {@code ExternalSort} using this {@code RunWriter}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	public RunWriter(Iterator<T> iterator, String fileName, int bufferSize, ExternalSort<T> externalSort)
			throws IOException {
		FileOutputStream out = new FileOutputStream(fileName); 
		do {
			try {
				byte[] bytes = write(iterator, bufferSize);
				out.write(bytes);
				externalSort.increaseBufferWriteCount();
			} catch (Exception e) {
				e.printStackTrace();
			}
		} while (iterator.hasNext());
		out.close();

	}

	/**
	 * Constructs an {@code OutputBuffer} and writes objects from the specified {@code Iterator} to that
	 * {@code OutputBuffer} until the {@code OutputBuffer} runs out of space.
	 * 
	 * @param iterator
	 *            an {@code Iterator}
	 * @param bufferSize
	 *            the size of the {@code OutputBuffer}
	 * @return the byte array containing the data that has been written to the {@code OutputBuffer}
	 * @throws IOException
	 *             if an I/O error occurs
	 */
	byte[] write(Iterator<T> iterator, int bufferSize) throws IOException {
		OutputBuffer b = new OutputBuffer(bufferSize);
		ObjectOutputStream out = new ObjectOutputStream(b);
		try {
			if (last != null)
				out.writeObject(last);
			while (iterator.hasNext()) {
				last = iterator.next();
				out.writeObject(last);
			}
		} catch (BufferOverflowException e) {
			return b.toByteArray();
		} finally {
			try {
				out.close();
			} catch (Exception e) {
			}
		}
		return b.toByteArray();
	}

}
