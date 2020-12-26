package edu.ncsu.csc316.rentals.manager;

import java.io.FileNotFoundException;
import java.util.Iterator;

import edu.ncsu.csc316.dsa.graph.Graph;
import edu.ncsu.csc316.dsa.graph.Graph.Edge;
import edu.ncsu.csc316.dsa.graph.Graph.Vertex;
import edu.ncsu.csc316.dsa.list.ArrayBasedList;
import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.dsa.map.Map;
import edu.ncsu.csc316.dsa.priority_queue.AdaptablePriorityQueue;
import edu.ncsu.csc316.dsa.priority_queue.PriorityQueue.Entry;
import edu.ncsu.csc316.dsa.set.Set;
import edu.ncsu.csc316.rentals.data.Rental;
import edu.ncsu.csc316.rentals.factory.DSAFactory;
import edu.ncsu.csc316.rentals.io.RentalReaderIO;

/**
 * RentalManager implements algorithms to sort the cheapest rental sequence and
 * get rentals for a specified day.
 * 
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class RentalManager {

	/** List containing the rentals */
	private List<Rental> rentals;

	/** The earliest start day for rentals */
	private int startDay;

	/** The last end day for rentals */
	private int endDay;

	/** Directed graph of rentals with start day as vertex and rentals as edges */
	private Graph<Integer, Rental> graph;
	
	/** Used for finding disconnected day */
	private Map<Vertex<Integer>, Edge<Rental>> forest;

	/**
	 * Constructs a new Rental manager with the given input file
	 * 
	 * @param pathToFile the path to the input CSV file
	 * 
	 * @throws FileNotFoundException if the file could not be found
	 */
	public RentalManager(String pathToFile) throws FileNotFoundException {
		this.rentals = RentalReaderIO.readFile(pathToFile);
		sortList(this.rentals);
		this.startDay = this.rentals.first().getStartDay();
		this.endDay = this.rentals.first().getEndDay();
		for (Rental r : this.rentals) {
			if (r.getStartDay() < this.startDay) {
				this.startDay = r.getStartDay();
			}
			if (r.getEndDay() > this.endDay) {
				this.endDay = r.getEndDay();
			}
		}
		this.graph = buildGraph(this.rentals);
	}

	/**
	 * Helper method used for creating the graph
	 * 
	 * @param rentalList the list of information for the graph
	 * 
	 * @return the new graph
	 */
	private Graph<Integer, Rental> buildGraph(List<Rental> rentalList) {
		Graph<Integer, Rental> g = DSAFactory.getDirectedGraph();
		Map<Integer, Vertex<Integer>> vertices = DSAFactory.getMap();
		for (Rental r : rentalList) {
			Vertex<Integer> u = vertices.get(r.getStartDay());
			if (u == null) {
				u = g.insertVertex(r.getStartDay());
				vertices.put(new Integer(r.getStartDay()), u);
			}
			Vertex<Integer> v = vertices.get(r.getEndDay());
			if (v == null) {
				v = g.insertVertex(r.getEndDay());
				vertices.put(new Integer(r.getEndDay()), v);
			}
			g.insertEdge(u, v, r);
		}

		return g;
	}

	/**
	 * Private method used for sorting a list using the merge sort algorithm
	 * 
	 * @param data the list to be sorted
	 */
	private void sortList(List<Rental> data) {

		int size = data.size();

		if (data.size() < 2) {
			return;
		}
		int mid = size / 2;

		List<Rental> left = new ArrayBasedList<>();
		for (int i = 0; i < mid; i++) {
			left.addLast(data.get(i));
		}

		List<Rental> right = new ArrayBasedList<>();
		for (int i = mid; i < size; i++) {
			right.addLast(data.get(i));
		}

		sortList(left);
		sortList(right);
		merge(left, right, data);
	}

	/**
	 * Helper method for sorting a list that has been split by the left range and
	 * the right range
	 * 
	 * @param left  the data from the 0 to the middle index - 1
	 * @param right the data from the middle to the end of the list
	 * @param data  the data being observed
	 */
	private void merge(List<Rental> left, List<Rental> right, List<Rental> data) {

		int leftIndex = 0;
		int rightIndex = 0;

		while ((leftIndex + rightIndex) < data.size()) {
			if (rightIndex == right.size()
					|| (leftIndex < left.size() && compare(left.get(leftIndex), right.get(rightIndex)) < 0)) {
				data.set(leftIndex + rightIndex, left.get(leftIndex));
				leftIndex++;
			} else {
				data.set(leftIndex + rightIndex, right.get(rightIndex));
				rightIndex++;
			}
		}
	}

	/**
	 * Method used for comparing rental objects. Sorted in ascending order by price.
	 * If multiple rentals have the same price, then further sort alphabetically by
	 * host last name, then host first name, then host email.
	 * 
	 * @param r the other object being compared against
	 * 
	 * @return 0 if the objects are the same 1 if the current object is follows the
	 *         parameter -1 if the current object precedes the parameter
	 */
	private int compare(Rental r1, Rental r2) {

		if (r1.getCost() > r2.getCost()) {
			return 1;
		} else if (r1.getCost() < r2.getCost()) {
			return -1;
		} else {
			if (r1.getLast().compareToIgnoreCase(r2.getLast()) != 0) {
				return r1.getLast().compareToIgnoreCase(r2.getLast());
			} else {
				if (r1.getFirst().compareToIgnoreCase(r2.getFirst()) != 0) {
					return r1.getFirst().compareToIgnoreCase(r2.getFirst());
				} else {
					if (r1.getEmail().compareToIgnoreCase(r2.getEmail()) != 0) {
						return r1.getEmail().compareToIgnoreCase(r2.getEmail());
					}
				}
			}
		}

		return 0;
	}

	/**
	 * Retrieves the current graph. Primarily used for testing
	 * 
	 * @return the graph of the Rental information
	 */
	public Graph<Integer, Rental> getGraph() {
		return graph;
	}

	/**
	 * Returns the String representation of the rentals that minimize the total cost
	 * from the start day to the end day (or for as many days from the start day
	 * while rentals are possible). NOTE: remember to check for valid start day,
	 * valid end day, and whether the graph is connected
	 * 
	 * @param start - the start day as an integer
	 * @param end   - the end day as an integer
	 * @return the String representation of the rentals that minimize cost
	 */
	public String getRentals(int start, int end) {

		if (start < this.startDay) {
			return "The specified start day (" + start + ") is smaller than the minimum day in the input data ("
					+ this.startDay + ").";
		}
		if (start > this.endDay) {
			return "The specified start day (" + start + ") is larger than the maximum day in the input data ("
					+ this.endDay + ").";
		}
		if (end < this.startDay) {
			return "The specified end day (" + end + ") is smaller than the minimum day in the input data ("
					+ this.startDay + ").";
		}
		if (end > this.endDay) {
			return "The specified end day (" + end + ") is larger than the maximum day in the input data ("
					+ this.endDay + ").";
		}

		// No specific flow for this but this can happen
		if (start >= end) {
			return "Invalid input: The start day is greater than or equal to the ending day.";
		}

		Map<Vertex<Integer>, Integer> distances = dijkstra(start);
		Map<Vertex<Integer>, Edge<Rental>> tree = shortestPathTree(start, distances);
		
		/*System.out.println(tree.size());
		Iterator<edu.ncsu.csc316.dsa.map.Map.Entry<Vertex<Integer>, Edge<Rental>>> it = tree.entrySet().iterator();
		while (it.hasNext()) {
			System.out.println(it.next().getValue());
		}*/

		
		
		List<Rental> list = DSAFactory.getIndexedList();
		
		// Check for connectedGraph correct Check US2 E5
		if (!checkConnectedGraph(start, end)) {
			// Vertex<Integer> u = findVertex(start);
			/*Set<Integer> set = DSAFactory.getSet();
			for (Vertex<Integer> x : tree ) {
				set.add(x.getElement());
			}
			
			// Set<Integer> set2 = DSAFactory.getSet();
			for (Vertex<Integer> v : graph.vertices()) {
				if (v.getElement() <= start || v.getElement() > end) {
					continue;
				}
				if (!set.contains(v.getElement())) {
					return "There are no rentals available on day " + v.getElement() + ".";
				}
			}*/
			
			Iterator<Vertex<Integer>> it = forest.iterator();
			int last = it.next().getElement();
			
			while (it.hasNext()) {
				int temp = it.next().getElement();
				if (temp > last) {
					last = temp;					
				}
			}
			
			return "There are no rentals available on day " + last + "."; 
		}

		Vertex<Integer> u = findVertex(end);
		// Edge<Rental> edge = graph.getEdge(u, v);

		int total = 0;
		while (!tree.isEmpty()) {
			Edge<Rental> edge = tree.remove(u);
//			System.out.println(edge);
			total += edge.getElement().getCost();
			list.addFirst(edge.getElement());
			u = graph.opposite(u, edge);
			if (u.getElement() == start) {
				break;
			}
		}

		StringBuilder out = new StringBuilder();
		out.append("Rental Total is $" + total + ".00 [\n");
		for (Rental r : list) {
			out.append("   $");
			out.append(r.getCost());
			out.append(".00 rental from day ");
			out.append(r.getStartDay());
			out.append(" to day ");
			out.append(r.getEndDay());
			out.append(" hosted by ");
			out.append(r.getFirst() + " ");
			out.append(r.getLast() + " (");
			out.append(r.getEmail() + ")");
			out.append("\n");
		}
		out.append("]");

		return out.toString();

	}

	// TODO Fix to work with use case 2 Alternative flow 5 (UC2 E5)
	// Temporarily commented out to obtain coverage and view TS Tests

	/**
	 * Checks if the graph is connected from the start and end day
	 * 
	 * @param startDay 	the starting day
	 * @param endDay	the ending day
	 * 
	 * @return	true	if the graph is connected
	 * 			false	otherwise
	 */
	private boolean checkConnectedGraph(int startDay, int endDay) {
		Vertex<Integer> u = findVertex(startDay);
		Vertex<Integer> v = findVertex(endDay);
		if (u == null || v == null) {
			return false;
		}
		
		Set<Vertex<Integer>> known = DSAFactory.getSet();
		this.forest = DSAFactory.getMap();
		dfsHelper(u, known, forest);
		
		if (known.contains(v)) {
			return true;
		}
		return false;
	}
	
	/**
	 * dfsHelper helps with depthFirstSearch algorithm.
	 * 
	 * @param graph  the graph containing the information
	 * @param v      the starting vertex
	 * @param known  the set of known vertices
	 * @param forest the forest of edges
	 * 
	 * @param <V> The Vertex
	 * @param <E> The Edge
	 */
	private void dfsHelper(Vertex<Integer> v, Set<Vertex<Integer>> known, Map<Vertex<Integer>, Edge<Rental>> forest) {
		known.add(v);
		for (Edge<Rental> edge : graph.outgoingEdges(v)) {
			Vertex<Integer> u = graph.opposite(v, edge);
			if (!known.contains(u)) {
				forest.put(u, edge);
				dfsHelper(u, known, forest);
			}
		}
	}

	/**
	 * Finds the total cost of the shortest path from a start day to other vertices.
	 * 
	 * @param startDay - the starting day 
	 * @param <V> - the value type
	 * @param <E> - the element type
	 * @return map of found vertex and integers values
	 */
	public <V, E extends Rental> Map<Vertex<Integer>, Integer> dijkstra(int startDay) {
		Vertex<Integer> src = findVertex(startDay);

		AdaptablePriorityQueue<Integer, Vertex<Integer>> pq = DSAFactory.getAdaptablePriorityQueue();

		Map<Vertex<Integer>, Integer> d = DSAFactory.getMap();
		Map<Vertex<Integer>, Integer> cloud = DSAFactory.getMap();
		Map<Vertex<Integer>, Entry<Integer, Vertex<Integer>>> pqTokens = DSAFactory.getMap();

		for (Vertex<Integer> v : graph.vertices()) {

			if (v.equals(src)) {
				d.put(v, 0);
			} else {
				d.put(v, Integer.MAX_VALUE);
			}

			pqTokens.put(v, pq.insert(d.get(v), v));
		}

		while (!pq.isEmpty()) {

			Entry<Integer, Vertex<Integer>> entry = pq.deleteMin();
			int key = entry.getKey();
			Vertex<Integer> u = entry.getValue();
			cloud.put(u, key);
			pqTokens.remove(u);

			for (Edge<Rental> e : graph.outgoingEdges(u)) {

				Vertex<Integer> v = graph.opposite(u, e);

				if (cloud.get(v) == null) {
					int weight = e.getElement().getWeight();

					if (d.get(u) + weight < d.get(v)) {
						d.put(v, d.get(u) + weight);
						pq.replaceKey(pqTokens.get(v), d.get(v));
					}
				}
			}
		}

		return cloud;
	}

	/**
	 * Finds the shortest path from a starting day to the other vertices.
	 * 
	 * @param startDay  The starting day to retrieve the shortest path
	 * @param distances Edges between nodes in the tree
	 * @param           <V> - the value type
	 * @param           <E> - the element type
	 * @return a map of vertices and integers
	 */
	public <V, E extends Rental> Map<Vertex<Integer>, Edge<Rental>> shortestPathTree(int startDay,
			Map<Vertex<Integer>, Integer> distances) {

		Vertex<Integer> s = findVertex(startDay);
		Map<Vertex<Integer>, Edge<Rental>> m = DSAFactory.getMap();

		for (Vertex<Integer> v : graph.vertices()) {
			if (!v.equals(s)) {
				for (Edge<Rental> e : graph.incomingEdges(v)) {
					Vertex<Integer> u = graph.opposite(v, e);

					if (distances.get(v).equals(distances.get(u) + e.getElement().getWeight())) {
						m.put(v, e);
					}
				}
			}
		}

		return m;

	}

	/**
	 * Returns the String representation of all the rentals that are available for
	 * the requested day.
	 * 
	 * @param day - the day for which to retrieve available rentals
	 * @return the String representation of the rentals
	 */
	public String getRentalsForDay(int day) {

		Vertex<Integer> u = findVertex(day);

		if (u == null) {
			return "The specified day (" + day + ") is larger than the maximum day in the input data ("
					+ graph.numVertices() + ").";
		}

		StringBuilder out = new StringBuilder();
		out.append("Available rentals for day ");
		out.append(day);
		out.append(" [\n");

		if (!graph.outgoingEdges(u).iterator().hasNext()) {
			out.append("   No rentals available.\n");
		} else {

			for (Edge<Rental> e : graph.outgoingEdges(u)) {

				out.append("   $");
				out.append(e.getElement().getCost());
				out.append(".00 rental from day ");
				out.append(day);
				out.append(" to day ");
				out.append(e.getElement().getEndDay());
				out.append(" hosted by ");
				out.append(e.getElement().getFirst() + " ");
				out.append(e.getElement().getLast() + " (");
				out.append(e.getElement().getEmail() + ")");
				out.append("\n");
			}
		}
		out.append("]");

		return out.toString();
	}

	/**
	 * Finds the vertex based on the entered day
	 * 
	 * @param day the day of the vertex
	 * 
	 * @return the Vertex of the entered day
	 */
	private Vertex<Integer> findVertex(int day) {
		for (Vertex<Integer> x : graph.vertices()) {
			if (x.getElement() == day) {
				return x;
			}
		}
		return null;
	}

}
