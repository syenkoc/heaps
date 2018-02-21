/*
 * $Id$
 * 
 * Copyright (c) 2005-2014 Fran Lattanzio
 * 
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 * 
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 * 
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */

package org.teneighty.heap;

import java.util.Comparator;
import java.util.Collection;
import java.util.ConcurrentModificationException;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * A heap interface.
 * <p>
 * Heaps can be used as very efficient priority queues. Heaps do not work well
 * as general purpose maps: They do not naturally support any sort of searching
 * operations or the deletion of arbitrary keys. In fact, the delete and
 * decrease key methods of this interface require that you have a reference to
 * the entry on which you wish to operate (as opposed to simply the key of the
 * entry). For most applications, this is not a problem... mostly.
 * <p>
 * A few notes:
 * <ol>
 *  <li>See the <a href="package-summary.html">Package Manifest</a> for a summary
 *      of the available implementations, as well as their runtime performance.</li>
 *  <li>In general, the order of items in the Sets/Collections returned from
 *      collection-view methods is generally arbitrary. However, it should always be
 *      the case that (barring any changes to the heap) they are consistent and
 *      deterministic.</li>
 *  <li>The iterators returned by implementations should generally be
 *      <i>fail-fast</i>, meaning they should detect changes to the backing heap and
 *      throw a {@link ConcurrentModificationException} if the backing heap is
 *      changed during iteration.</li>
 *  <li>Heaps do not maintain insertion order between elements with equal keys.
 *      This is not the general contract of the heap ADT. If you need this
 *      functionality, it should be programmed externally.</li>
 *  <li>It is generally not a problem to use as a key {@link Comparable} class
 *      whose {@link Comparable#compareTo(Object)} method is inconsistent with
 *      {@link Object#equals(Object)}, since we don't care about equal key
 *      collisions. Similarly, a {@link Comparator} whose
 *      {@link Comparator#compare(Object, Object)} method is inconsistent with equals
 *      is also OK.</li>
 *  <li>Be <i>very</i> careful if you use mutable objects as keys. If the
 *      properties of a key object change in such a way that it affects the outcome
 *      the <code>compareTo(Object)</code>/
 *      <code>Comparator.compare(Object, Object)</code> methods, you will probably
 *      smash the structure beyond repair. You can, of course change the key
 *      associated with a given {@link Heap.Entry} but only through the use of the
 *      {@link #decreaseKey(Entry, Object)} method.</li>
 * </ol>
 * 
 * @param <TKey> the key type.
 * @param <TValue> the value type.
 * @see org.teneighty.heap.Heaps
 */
public interface Heap<TKey, TValue>
	extends Iterable<Heap.Entry<TKey, TValue>>
{

	/**
	 * Get the comparator used for decision in this heap.
	 * <p>
	 * If this method returns <code>null</code> then this heap uses the keys'
	 * <i>natural ordering</i>.
	 * 
	 * @return the comparator or <code>null</code>.
	 * @see java.util.Comparator
	 * @see java.lang.Comparable
	 */
	public Comparator<? super TKey> getComparator();

	/**
	 * Get the number of entries in this heap.
	 * 
	 * @return the number of entries in this heap.
	 */
	public int getSize();

	/**
	 * Is this heap empty?
	 * 
	 * @return <code>true</code> if this heap is empty; <code>false</code>
	 *         otherwise.
	 * @see #getSize()
	 */
	public boolean isEmpty();

	/**
	 * Does this heap contain the specified entry? In other words, does this
	 * heap contain an entry <code>e</code> such that
	 * <code>e.equals( entry ) == true</code>. Note that this does <b>not</b>
	 * imply that <code>e == entry</code>: See {@link Heap.Entry#equals(Object)}
	 * <p>
	 * This method generally takes <code>O(n)</code> time, although you should
	 * check the notes of the specific implementation you are using.
	 * 
	 * @param entry the entry to check.
	 * @return <code>true</code> if this heap contains the specified entry;
	 *         <code>false</code> otherwise.
	 * @throws NullPointerException If <code>entry</code> is <code>null</code>.
	 */
	public boolean containsEntry(Entry<TKey, TValue> entry);

	/**
	 * Add a key/value pair to this heap.
	 * 
	 * @param key the node key.
	 * @param value the node value.
	 * @return the entry created.
	 * @throws ClassCastException If the specified key is not mutually
	 *         comparable with the other keys of this heap.
	 * @throws NullPointerException If <code>key</code> is <code>null</code> and
	 *         this heap does not support <code>null</code> keys.
	 */
	public Entry<TKey, TValue> insert(TKey key, TValue value);

	/**
	 * Insert shallow copies all the entries of the specified heap into this
	 * heap, even if the specified heaps are unionable.
	 * <p>
	 * Invoking this method will not change the contents of <code>other</code>.
	 * 
	 * @param other the other heap.
	 * @throws NullPointerException If <code>other</code> is <code>null</code>.
	 * @throws ClassCastException If the keys of <code>other</code> are not
	 *             mutually comparable to the keys of this heap.
	 * @throws IllegalArgumentException If you attempt to insert a heap into
	 *             itself.
	 * @see #union(Heap)
	 */
	public void insertAll(Heap<? extends TKey, ? extends TValue> other);

	/**
	 * Union this heap with another heap.
	 * <p>
	 * Only instances of the same class are capable of being unioned together.
	 * This is a change from previous versions, when the union of different
	 * types resulting in {@link #insertAll(Heap)} type behavior. However, this
	 * meant that the union method had different semantics based on the
	 * runtime-type of the other heap, which is definitely a bad thing.
	 * <p>
	 * After a union operation, this heap will both <i>contain</i> and
	 * <i>hold</i> the entries of the other heap. The other heap is cleared in
	 * the process of union.
	 * 
	 * @param other the other heap.
	 * @throws NullPointerException If <code>other</code> is <code>null</code>.
	 * @throws ClassCastException If the keys of the nodes are not mutually
	 *         comparable or the classes do not match.
	 * @throws IllegalArgumentException If you attempt to union a heap with
	 *         itself (i.e if <code>other == this</code>).
	 * @see #insertAll(Heap)
	 */
	public void union(Heap<TKey, TValue> other);

	/**
	 * Get the entry with the minimum key.
	 * <p>
	 * This method does <u>not</u> remove the returned entry.
	 * 
	 * @return the entry.
	 * @throws NoSuchElementException If this heap is empty.
	 * @see #extractMinimum()
	 */
	public Entry<TKey, TValue> getMinimum()
		throws NoSuchElementException;

	/**
	 * Remove and return the entry minimum key.
	 * 
	 * @return the entry.
	 * @throws NoSuchElementException If the heap is empty.
	 * @see #getMinimum()
	 */
	public Entry<TKey, TValue> extractMinimum();

	/**
	 * Decrease the key of the given element.
	 * <p>
	 * Note that <code>e</code> must be <i>held</i> by this heap, or a
	 * <code>IllegalArgumentException</code> will be tossed.
	 * 
	 * @param e the entry for which to decrease the key.
	 * @param key the new key.
	 * @throws IllegalArgumentException If <code>k</code> is larger than
	 *         <code>e</code>'s current key or <code>e</code> is not held by
	 *         this heap.
	 * @throws ClassCastException If the new key is not mutually comparable with
	 *         other keys in the heap.
	 * @throws NullPointerException If <code>e</code> is <code>null</code>.
	 */
	public void decreaseKey(Entry<TKey, TValue> e, TKey key);

	/**
	 * Delete the entry from this heap.
	 * <p>
	 * Note that <code>e</code> must be held by this heap, or an
	 * <code>IllegalArgumentException</code> will be tossed.
	 * 
	 * @param e the entry to delete.
	 * @throws IllegalArgumentException If <code>e</code> is not held by this
	 *         heap.
	 * @throws NullPointerException If <code>e</code> is <code>null</code>.
	 */
	public void delete(Entry<TKey, TValue> e);

	/**
	 * Clear this heap.
	 */
	public void clear();
	
	/**
	 * Get an iterator over the entries of this heap.
	 * 
	 * @return an iterator over the entries of this heap.
	 */
	@Override
	public Iterator<Heap.Entry<TKey, TValue>> iterator();

	/**
	 * Get the collection of keys.
	 * <p>
	 * The order of the keys in returned collection is arbitrary.
	 * 
	 * @return the keys.
	 */
	public Collection<TKey> getKeys();

	/**
	 * Get the collection of values.
	 * <p>
	 * The order of the values in returned collection is arbitrary.
	 * 
	 * @return the values.
	 */
	public Collection<TValue> getValues();

	/**
	 * Get the entry collection.
	 * <p>
	 * The order of the entries in the returned collection is arbitrary.
	 * 
	 * @return the entry collection.
	 * @see org.teneighty.heap.Heap.Entry
	 */
	public Collection<Heap.Entry<TKey, TValue>> getEntries();

	/**
	 * Compare this heap for equality with the specified object.
	 * <p>
	 * Equality for two heaps is defined to be that they <i>contain</i>, not
	 * <i>hold</i>, the exact same set of entries. (Otherwise, two heaps could
	 * never be equal, unless they were the same object. This should be obvious
	 * from the definitions of <i>holds</i> and <i>contains</i>.) See
	 * {@link Heap.Entry#equals(Object)} for the definition of
	 * <code>Entry</code> equality. This definition is not open to debate.
	 * <p>
	 * Efficiency of this method interesting question, since it depends only on
	 * which elements are stored, not <u>how</u> they are stored. For example,
	 * it's difficult to efficiently compare a {@link FibonacciHeap Fibonacci
	 * heap} and a {@link BinomialHeap Binomial heap}, even if they contain the
	 * same elements, since their underlying representations are vastly
	 * different. (In fact, it's very difficult to compare two Fibonacci heaps
	 * with the same set of entries!)
	 * 
	 * @param other the other object.
	 * @return <code>true</code> if equal; <code>false</code> otherwise.
	 * @see Object#equals(Object)
	 * @see #hashCode()
	 */
	@Override
	public boolean equals(Object other);

	/**
	 * Return the hashcode for this Heap.
	 * <p>
	 * The hashcode for <i>any</i> heap is hereby defined to be sum of the
	 * hashcodes of the entries which this heap <i>holds</i>. Like the equality
	 * definition, this is not debatable. Note that this definition does not
	 * violate the definition of <code>equals</code>, since if a heap
	 * <i>holds</i> a set of entries it must also <i>contain</i> them.
	 * <p>
	 * If you choose to override the equals method, you must also override this
	 * method, unless you really want your objects to violate the general
	 * contract of <code>Object</code>.
	 * 
	 * @return the hashcode.
	 * @see java.lang.Object#hashCode()
	 * @see #equals(Object)
	 */
	@Override
	public int hashCode();
	
	/**
	 * The heap entry interface.
	 * 
	 * @param <TKey> the key type.
	 * @param <TValue> the value type.
	 */
	public static interface Entry<TKey, TValue>
	{

		/**
		 * Get the key of this entry.
		 * 
		 * @return the key.
		 */
		public TKey getKey();

		/**
		 * Get the value of this entry.
		 * 
		 * @return he value.
		 * @see #setValue(Object)
		 */
		public TValue getValue();

		/**
		 * Set the value of this entry.
		 * 
		 * @param value The new value.
		 * @return The old value.
		 * @see #getValue()
		 */
		public TValue setValue(TValue value);

		/**
		 * A reminder to override equals.
		 * <p>
		 * Two entries are defined to be equal iff they contains exactly equal
		 * key and value objects (or <code>null</code>). Again, this definition
		 * is not open to debate.
		 * 
		 * @param other The object to which to compare.
		 * @return <code>true</code> if equal; <code>false</code> otherwise.
		 * @see java.lang.Object#equals(Object)
		 */
		@Override
		public boolean equals(Object other);

		/**
		 * A reminder to override hashcode.
		 * <p>
		 * The hashcode of a heap entry is defined to be the hashcodes of this
		 * entry's key and value objects (or 0 if these objects are
		 * <code>null</code>) XOR'ed with each other.
		 * 
		 * @return The hash code for this object.
		 * @see java.lang.Object#hashCode()
		 * @see #equals(Object)
		 */
		@Override
		public int hashCode();

	}

}
