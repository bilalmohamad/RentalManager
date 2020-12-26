package edu.ncsu.csc316.rentals.factory;

import edu.ncsu.csc316.dsa.graph.AdjacencyListGraph;
import edu.ncsu.csc316.dsa.graph.Graph;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.list.SinglyLinkedList;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.map.hashing.LinearProbingHashMap;
import edu.ncsu.csc316.dsa.priority_queue.AdaptablePriorityQueue;
import edu.ncsu.csc316.dsa.priority_queue.HeapAdaptablePriorityQueue;
import edu.ncsu.csc316.dsa.set.HashSet;
import edu.ncsu.csc316.dsa.set.Set;

/**
 * Factory for creating new data structure and algorithm instances
 * 
 * @author Dr. King
 *
 */
public class DSAFactory {

	/**
	 * Returns a data structure that implements a map
	 * 
	 * @param <K> - the key type
	 * @param <V> - the value type
	 * @return a data structure that implements a map
	 */
	public static <K, V> Map<K, V> getMap() {
		return new LinearProbingHashMap<K, V>();
	}

	/**
	 * Returns a data structure that implements an index-based list
	 * 
	 * @param <E> - the element type
	 * @return an index-based list
	 */
	public static <E> List<E> getIndexedList() {
		return getSinglyLinkedList();
	}

	/**
	 * Returns a data structure that implements an Adaptable Priority Queue
	 * 
	 * @param <K> - the key type
	 * @param <V> - the value type
	 * @return an adaptable priority queue
	 */
	public static <K extends Comparable<K>, V> AdaptablePriorityQueue<K, V> getAdaptablePriorityQueue() {
		return new HeapAdaptablePriorityQueue<K, V>();
	}

	/**
	 * Returns a data structure that implements a Set
	 * 
	 * @param <E> - the element type
	 * @return a set
	 */
	public static <E> Set<E> getSet() {
		return new HashSet<E>();
	}

	/**
	 * Returns a data structure that implements a directed graph
	 * 
	 * @param <E> - the element type
	 * @param <V> - the value type
	 * @return a directed graph
	 */
	public static <V, E> Graph<V, E> getDirectedGraph() {
		return new AdjacencyListGraph<V, E>(true);
	}

	/**
	 * Returns a singly linked list with front pointer
	 * 
	 * @return a singly linked list with front pointer
	 */
	private static <E> SinglyLinkedList<E> getSinglyLinkedList() {
		return new SinglyLinkedList<E>();
	}

}
