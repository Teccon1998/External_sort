package external_sort;

import java.io.IOException;
import java.io.OutputStream;

/**
 * An {@code OutputBuffer} is an {@code OutputStream} that writes data to a fixed size byte array.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 */
public class OutputBuffer extends OutputStream {

	/**
	 * An {@code BufferOverflowException} is thrown when an {@code OutputBuffer} does not have sufficient space for the
	 * data to be written
	 * 
	 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
	 *
	 */
	public class BufferOverflowException extends IOException {

		/**
		 * An automatically generated serial version UID.
		 */
		private static final long serialVersionUID = -4415190846530385401L;

	}

	/**
	 * A byte array for keeping the data written to this {@code OutputBuffer}.
	 */
	byte[] buffer;

	/**
	 * The number of bytes written to this {@code OutputBuffer}.
	 */
	int count = 0;

	/**
	 * Constructs an {@code OutputBuffer}.
	 * 
	 * @param bufferSize
	 *            the size of the {@code OutputBuffer} (i.e., the size of the byte array for keeping the data written to
	 *            the {@code OutputBuffer})
	 */
	public OutputBuffer(int bufferSize) {
		buffer = new byte[bufferSize];
	}

	/**
	 * Returns the byte array containing the data that has been written to this {@code OutputBuffer}.
	 *
	 * @return the byte array containing the data that has been written to this {@code OutputBuffer}
	 */
	public byte[] toByteArray() {
		return buffer;
	}

	/**
	 * Writes the specified byte to this {@code OutputBuffer}.
	 *
	 * @param b
	 *            the byte to be written
	 * @throws BufferOverflowException
	 *             if this {@code OutputBuffer} does not have sufficient space for the byte
	 */
	@Override
	public void write(int b) throws BufferOverflowException {
		if (count >= buffer.length)
			throw new BufferOverflowException();
		buffer[count] = (byte) b;
		count += 1;
	}

	/**
	 * Writes <code>len</code> bytes from the specified byte array starting at offset <code>off</code> to this
	 * {@code OutputBuffer}.
	 *
	 * @param b
	 *            the data
	 * @param off
	 *            the start offset in the data
	 * @param len
	 *            the number of bytes to write
	 * @throws BufferOverflowException
	 *             if this {@code OutputBuffer} does not have sufficient space for the data to be written
	 */
	@Override
	public void write(byte b[], int off, int len) throws BufferOverflowException {
		if ((off < 0) || (off > b.length) || (len < 0) || ((off + len) - b.length > 0)) {
			throw new IndexOutOfBoundsException();
		}
		if (count + len >= buffer.length)
			throw new BufferOverflowException();
		System.arraycopy(b, off, buffer, count, len);
		count += len;
	}
	
}
