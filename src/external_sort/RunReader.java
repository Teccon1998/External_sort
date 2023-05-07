package external_sort;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A {@code RunReader} reads objects from a run. A {@code RunReader} can also be viewed as an {@code Iterator} over the
 * objects stored in a run.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class RunReader implements Iterator<Object> {

	/**
	 * The {@code ExternalSort} that uses this {@code RunReader}.
	 */
	ExternalSort<?> externalSort;

	/**
	 * The input buffer size
	 */
	int bufferSize;

	/**
	 * A {@code FileInputStream} for reading data from the run (i.e., the file)
	 */
	FileInputStream in;

	/**
	 * The length of the run.
	 */
	long runLength;

	/**
	 * The number of bytes read from the run.
	 */
	long bytesRead = 0;

	/**
	 * Constructs a {@code RunReader}.
	 * 
	 * @param fileName
	 *            the name of the file (i.e., the run)
	 * @param bufferSize
	 *            the input buffer size
	 * @param externalSort
	 *            the {@code ExternalSort} that uses the {@code RunReader}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 * 
	 */
	public RunReader(String fileName, int bufferSize, ExternalSort<?> externalSort)
			throws IOException, ClassNotFoundException {
		this.externalSort = externalSort;
		this.bufferSize = bufferSize;
		File file = new File(fileName);
		runLength = file.length();
		in = new FileInputStream(file);
	}

	/**
	 * Constructs an {@code InputBuffer} and fills it using the data from the run.
	 * 
	 * @return the filled {@code InputBuffer}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	InputBuffer read() throws IOException, ClassNotFoundException {
		InputBuffer b = new InputBuffer(bufferSize);
		bytesRead += in.read(b.toByteArray());
		externalSort.increaseBufferReadCount();
		return b;
	}

	/**
	 * Determines whether or not this {@code RunReader} has more objects to iterate over (i.e., {@link #next} would
	 * return an object rather than throwing an exception).
	 *
	 * @return {@code true} if this {@code RunReader} has more objects to iterate over; {@code false} otherwise
	 */
	@Override
	public boolean hasNext() {
		try {
			if(externalSort.iterator!= null)
			{
				if(externalSort.iterator.hasNext())
				{
					return true;
				}
					
				return false;
			}
			else
			{
				InputBuffer buffer = read();
				Iterator iterator = buffer.iterator();
				externalSort.iterator = iterator;
				return hasNext();
			}
			///////
		// 	externalSort.iterator = (Iterator<?>) read().iterator();
		// 	if (buffer != null) {
		// 		Iterator iter = buffer.iterator();
		// 		if(iter.hasNext())
		// 			return true;
		// 		return false;
		// 	} else {
		// 		// the run is empty or has been fully read
		// 		in.close();
		// 		return false;
		// 	}
		// } catch (IOException | ClassNotFoundException e) {
		// 	e.printStackTrace();
		// 	return false;
		// }
		}
		catch(Exception e)
		{
			e.printStackTrace();
			return false;
		}
	}

	/**
	 * Returns the next object in the iteration.
	 *
	 * @return the next object in the iteration
	 * @throws NoSuchElementException
	 *             if the iteration has no more objects to iterate over
	 */
	/**
 * Returns the next object in the iteration.
 *
 * @return the next object in the iteration
 * @throws NoSuchElementException
 *             if the iteration has no more objects to iterate over
 */
	@Override
	public Object next() throws NoSuchElementException {
		if(externalSort.iterator != null)
		{
			if(externalSort.iterator.hasNext())
					return externalSort.iterator.next();
				return null;
		}
		else
		{
			InputBuffer buffer;
			try {
				buffer = read();
				Iterator iterator = buffer.iterator();
				externalSort.iterator = iterator;
				return next();
			} catch (ClassNotFoundException | IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		throw new NoSuchElementException("No more elements in the run.");
		// try {
		// 	InputBuffer buffer = read();
		// 	if (buffer != null) {
		// 		Iterator<Object> iter = buffer.iterator();
		// 		if (iter.hasNext()) {
		// 			return iter.next();
		// 		} else {
		// 			// the buffer is empty; try to read the next one
		// 			return next();
		// 		}
		// 	} else {
		// 		// the run is empty or has been fully read
		// 		in.close();
		// 		throw new NoSuchElementException("No more elements in the run.");
		// 	}
		// } catch (IOException | ClassNotFoundException e) {
		// 	throw new NoSuchElementException(e.getMessage());
		// }
	}

		
}
