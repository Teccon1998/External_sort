package external_sort;

import java.util.AbstractMap;
import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Map.Entry;
import java.util.PriorityQueue;

/**
 * Given multiple {@code Iterator}s each of which iterates over elements in ascending order according to the
 * {@linkplain Comparable natural ordering} of the elements, an {@code OrderedMergeIterator} iterates over all of these
 * elements in ascending order.
 * 
 * @author Jeong-Hyon Hwang (jhh@cs.albany.edu)
 *
 * @param <T>
 *            the type of the elements
 */
public class OrderedMergeIterator<T extends Comparable<? super T>> implements Iterator<T> {

	/**
	 * A priority queue for storing {@code Iterator}s while setting the priority of each {@code Iterator} to the last
	 * element obtained from that {@code Iterator}.
	 */
	PriorityQueue<Map.Entry<T, Iterator<T>>> queue = new PriorityQueue<Map.Entry<T, Iterator<T>>>(
			new Comparator<Map.Entry<T, Iterator<T>>>() {

				@Override
				public int compare(Entry<T, Iterator<T>> o1, Entry<T, Iterator<T>> o2) {
					return o1.getKey().compareTo(o2.getKey());
				}

			});

	/**
	 * Constructs an {@code OrderedMergeIterator}.
	 * 
	 * @param iterators
	 *            {@code Iterator}s each of which iterates over elements in ascending order according to the
	 *            {@linkplain Comparable natural ordering} of the elements
	 */
	public OrderedMergeIterator(Iterable<Iterator<T>> iterators) {
		for (Iterator<T> iterator : iterators) {
			if (iterator.hasNext())
				queue.add(new AbstractMap.SimpleEntry<T, Iterator<T>>(iterator.next(), iterator));
		}
	}

	/**
	 * Determines whether or not the iteration has more elements (i.e., {@link #next} would return an element rather
	 * than throwing an exception).
	 *
	 * @return {@code true} if the iteration has more elements; {@code false} otherwise
	 */
	@Override
	public boolean hasNext() {
		return !queue.isEmpty();
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
		Map.Entry<T, Iterator<T>> e = queue.poll();
		if (e == null)
			throw new NoSuchElementException();
		// TODO complete this method (10 points)
		throw new UnsupportedOperationException();
	}

}
