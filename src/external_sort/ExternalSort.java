package external_sort;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.NoSuchElementException;

/**
 * An {@code ExternalSort} sorts data elements in ascending order according to the {@linkplain Comparable natural
 * ordering} of the elements. An {@code ExternalSort} can also be used as an {@code Iterator} over all of these sorted
 * data elements.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 *
 * @param <T>
 *            the type of the elements
 */
public abstract class ExternalSort<T extends Comparable<? super T>> implements Iterator<T> {

	/**
	 * The number of runs that are merged together to form a new run
	 */
	int degree;

	/**
	 * The buffer size.
	 */
	int bufferSize;

	/**
	 * The prefix that all of the run names (i.e., the names of the run files) commonly have
	 */
	String fileNamePrefix;

	/**
	 * The output iterator of (i.e., the iterator over all elements that are sorted by) this {@code ExternalSort}.
	 */
	Iterator<T> iterator;

	/**
	 * The ID of the next run.
	 */
	int nextRunID = 0;

	/**
	 * The number of buffer reads so far.
	 */
	long bufferReadCount = 0;

	/**
	 * The number of buffer writes so far.
	 */
	long bufferWriteCount = 0;

	/**
	 * Constructs an {@code ExternalSort}
	 * 
	 * @param iterator
	 *            the input {@code Iterator}
	 * @param degree
	 *            the number of runs that are merged together to form a new run
	 * @param bufferSize
	 *            the buffer size
	 * @param fileNamePrefix
	 *            the prefix that all of the run names (i.e., the names of the run files) will commonly have
	 * @param out
	 *            a {@code PrintStream}
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	public ExternalSort(Iterator<T> iterator, int degree, int bufferSize, String fileNamePrefix, PrintStream out)
			throws IOException, ClassNotFoundException {
		if (iterator == null)
			return;
		this.fileNamePrefix = fileNamePrefix;
		this.degree = degree;
		this.bufferSize = bufferSize;

		out.print("initial pass: ");
		long time = System.nanoTime();
		ArrayList<String> runNames = createInitialRuns(iterator);
		out.println(runNames.size() + " runs" + String.format(" (%.5f seconds)", (System.nanoTime() - time) * 1.0e-9));
		int mergePass = 0;
		while (runNames.size() > degree) {
			out.print("merge pass " + (++mergePass) + ": ");
			time = System.nanoTime();
			runNames = merge(runNames);
			out.println(runNames.size() + " run(s)"
					+ String.format(" (%.5f seconds)", (System.nanoTime() - time) * 1.0e-9));
		}
		out.print("merge pass " + (++mergePass) + ": ");
		this.iterator = new OrderedMergeIterator<T>(createRunReaders(runNames));
		out.println("1 output iterator");
	}

	/**
	 * Merges the specified input runs into new runs
	 * 
	 * @param runNames
	 *            the name of the input runs (i.e., file names)
	 * @return the names of the new runs (i.e., file names)
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	ArrayList<String> merge(List<String> runNames) throws IOException, ClassNotFoundException {
		// TODO complete this method (5 points)
		throw new UnsupportedOperationException();
	}

	/**
	 * Constructs a new run by merging input runs.
	 * 
	 * @param runNames
	 *            the names of the input runs.
	 * @return the name of the newly constructed run
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	String createRun(List<String> runNames) throws IOException, ClassNotFoundException {
		if (runNames.size() == 1)
			return runNames.get(0);
		return createRun(new OrderedMergeIterator<T>(createRunReaders(runNames)));
	}

	/**
	 * Constructs {@code RunReader}s for the specified runs.
	 * 
	 * @param runNames
	 *            the names of the runs (i.e., the file names)
	 * @return the constructed {@code RunReader}s
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	@SuppressWarnings("unchecked")
	Iterable<Iterator<T>> createRunReaders(List<String> runNames) throws IOException, ClassNotFoundException {
		ArrayList<Iterator<T>> iterators = new ArrayList<Iterator<T>>();
		for (String runName : runNames)
			iterators.add((Iterator<T>) new RunReader(runName, bufferSize, this));
		return iterators;
	}

	/**
	 * Determines whether or not the iteration has more elements (i.e., {@link #next} would return an element rather
	 * than throwing an exception).
	 *
	 * @return {@code true} if the iteration has more elements; {@code false} otherwise
	 */
	@Override
	public boolean hasNext() {
		return iterator.hasNext();
	}

	/**
	 * Returns the next element in the iteration.
	 *
	 * @return the next element in the iteration
	 * @throws NoSuchElementException
	 *             if the iteration has no more elements
	 */
	@Override
	public T next() {
		return iterator.next();
	}

	protected abstract boolean isFull(ArrayList<T> list);

	/**
	 * Constructs initial runs using the data from the specified input {@code Iterator}.
	 * 
	 * @param iterator
	 *            the input {@code Iterator}
	 * @return the names of the initial runs constructed (i.e., file names)
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	ArrayList<String> createInitialRuns(Iterator<T> iterator)
			throws FileNotFoundException, IOException, ClassNotFoundException {
		ArrayList<T> list = new ArrayList<T>(); // a temporary list for storing elements from the input iterator
		ArrayList<String> runNames = new ArrayList<String>();
		while (iterator.hasNext()) {
			list.add(iterator.next());
			if (isFull(list) || !iterator.hasNext()) {
				Collections.sort(list); // sort the elements in the list
				runNames.add(createRun(list.iterator())); // construct a run containing the elements in the list and
															// register the name of the run
				list.clear();
			}
		}
		return runNames;
	}

	/**
	 * Constructs a new run using the data from the specified input {@code Iterator}.
	 * 
	 * @param iterator
	 *            the input {@code Iterator}
	 * @return the name of the newly constructed run
	 * @throws IOException
	 *             if an I/O error occurs
	 * @throws ClassNotFoundException
	 *             if the class of a serialized object cannot be found
	 */
	String createRun(Iterator<T> iterator) throws IOException, ClassNotFoundException {
		String fileName = fileNamePrefix + (nextRunID++) + ".run";
		new RunWriter<T>(iterator, fileName, bufferSize, this);
		return fileName;
	}

	/**
	 * Returns the number of buffer reads.
	 * 
	 * @return the number of buffer reads
	 */
	public long bufferReadCount() {
		return bufferReadCount;
	}

	/**
	 * Returns the number of buffer writes.
	 * 
	 * @return the number of buffer writes
	 */
	public long bufferWriteCount() {
		return bufferWriteCount;
	}

	/**
	 * Increases the number of buffer reads.
	 */
	void increaseBufferReadCount() {
		bufferReadCount++;
	}

	/**
	 * Increases the number of buffer writes.
	 */
	void increaseBufferWriteCount() {
		bufferWriteCount++;
	}

	/**
	 * Returns the buffer size.
	 * 
	 * @return the buffer size
	 */
	public int bufferSize() {
		return bufferSize;
	}

	/**
	 * Returns the number of bytes read.
	 * 
	 * @return the number of bytes read
	 */
	public long bytesRead() {
		return bufferReadCount * bufferSize;
	}

	/**
	 * Returns the number of bytes written.
	 * 
	 * @return the number of bytes written
	 */
	public long bytesWritten() {
		return bufferWriteCount * bufferSize;
	}

}
