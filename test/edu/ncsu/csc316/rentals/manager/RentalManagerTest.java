package edu.ncsu.csc316.rentals.manager;

import static org.junit.Assert.assertEquals;

import java.io.FileNotFoundException;

import org.junit.Test;

import edu.ncsu.csc316.dsa.graph.Graph;
import edu.ncsu.csc316.rentals.data.Rental;

/**
 * Tests the RentalManager class.
 * 
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class RentalManagerTest {

	/** The input file to read input from */
	public static final String SAMPLE = "input/sample.csv";
	
	/** A more complex testing file to read input from */
	public static final String COMPLEX = "input/complex.csv";

	/**
	 * Tests the built graph
	 * 
	 * @throws FileNotFoundException if the file can not be found
	 */
	@Test
	public void testGraph() throws FileNotFoundException {

		RentalManager rm = new RentalManager(SAMPLE);
		Graph<Integer, Rental> g = rm.getGraph();
		assertEquals(10, g.numEdges());
		assertEquals(5, g.numVertices());

		/*
		 * for (Edge<Rental> e : g.edges()) { System.out.println(e); }
		 */
	}

	/**
	 * Tests the getRentalsForDay method
	 * 
	 * @throws FileNotFoundException if the file could not be found
	 */
	@Test
	public void testGetRentalsForDay() throws FileNotFoundException {

		RentalManager rm = new RentalManager(SAMPLE);

		assertEquals("The specified day (6) is larger than the maximum day in the input data (5).",
				rm.getRentalsForDay(6));
		assertEquals("Available rentals for day 5 [\n   No rentals available.\n]", rm.getRentalsForDay(5));
		assertEquals(
				"Available rentals for day 4 [\n   $50.00 rental from day 4 to day 5 hosted by Suzanne Balik (sbalik@email.com)\n]",
				rm.getRentalsForDay(4));

//		System.out.println(rm.getRentalsForDay(4));
	}

	/**
	 * Tests the getRentals method
	 * 
	 * @throws FileNotFoundException if the file could not be found
	 */
	@Test
	public void testGetRentals() throws FileNotFoundException {

		RentalManager rm = new RentalManager(SAMPLE);

		assertEquals("Invalid input: The start day is greater than or equal to the ending day.", rm.getRentals(2, 2));

		assertEquals("The specified start day (0) is smaller than the minimum day in the input data (1).",
				rm.getRentals(0, 4));
		assertEquals("The specified start day (6) is larger than the maximum day in the input data (5).",
				rm.getRentals(6, 5));

		assertEquals("The specified end day (0) is smaller than the minimum day in the input data (1).",
				rm.getRentals(1, 0));
		assertEquals("The specified end day (6) is larger than the maximum day in the input data (5).",
				rm.getRentals(1, 6));

		// System.out.println(rm.getRentals(1, 5));

		String day1to5 = "Rental Total is $225.00 [\n"
				+ "   $85.00 rental from day 1 to day 2 hosted by Jason King (jtking@email.com)\n"
				+ "   $90.00 rental from day 2 to day 4 hosted by Ignacio Dominguez (idominguez@email.com)\n"
				+ "   $50.00 rental from day 4 to day 5 hosted by Suzanne Balik (sbalik@email.com)\n" + "]";

		assertEquals(day1to5, rm.getRentals(1, 5));

		String day2to5 = "Rental Total is $140.00 [\n"
				+ "   $90.00 rental from day 2 to day 4 hosted by Ignacio Dominguez (idominguez@email.com)\n"
				+ "   $50.00 rental from day 4 to day 5 hosted by Suzanne Balik (sbalik@email.com)\n" + "]";

		assertEquals(day2to5, rm.getRentals(2, 5));

		String day3to5 = "Rental Total is $90.00 [\n"
				+ "   $90.00 rental from day 3 to day 5 hosted by Lina Battestilli (lbattestilli@email.com)\n" + "]";

		assertEquals(day3to5, rm.getRentals(3, 5));
	}

	/**
	 * Tests an input file with disconnected days.
	 * 
	 * @throws FileNotFoundException if the file could not be found
	 */
	@Test
	public void testComplexRentals() throws FileNotFoundException {
		RentalManager rm = new RentalManager(COMPLEX);

		String day1to6 = "There are no rentals available on day 5.";

		assertEquals(day1to6, rm.getRentals(1, 6));

		String day1to7 = "There are no rentals available on day 5.";

		assertEquals(day1to7, rm.getRentals(1, 7));

		String day6to7 = "Rental Total is $25.00 [\n"
				+ "   $25.00 rental from day 6 to day 7 hosted by Marwah Mahate (msmahate@ncsu.edu)\n" + "]";
		assertEquals(day6to7, rm.getRentals(6, 7));
		
		assertEquals(day1to7, rm.getRentals(4, 7));
	}
}
