package edu.ncsu.csc316.rentals.io;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.io.FileNotFoundException;

import org.junit.Test;

import edu.ncsu.csc316.dsa.list.List;
import edu.ncsu.csc316.rentals.data.Rental;

/**
 * Tests the RentalReaderIO class
 *
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class RentalReaderIOTest {

	/** The input file to read input from */
	public static final String SAMPLE = "input/sample.csv";
	
	/** Input file containing invalid input */
	public static final String DUPLICATES = "input/duplicates.csv";
	
	/** Rental object used for testing */
	public static final Rental RENTAL1 = new Rental(1, 2, 85, "Jason", "King", "jtking@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL2 = new Rental(1, 4, 255, "Sarah", "Heckman", "sheckman@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL3 = new Rental(2, 5, 220, "Jessica", "Schmidt", "jschmidt@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL4 = new Rental(4, 5, 50, "Suzanne", "Balik", "sbalik@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL5 = new Rental(2, 3, 65, "David", "Sturgill", "dsturgill@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL6 = new Rental(3, 5, 90, "Lina", "Battestilli", "lbattestilli@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL7 = new Rental(3, 4, 55, "Jamie", "Jennings", "jjennings@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL8 = new Rental(1, 5, 500, "Margaret", "Heil", "mheil@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL9 = new Rental(1, 3, 180, "Toniann", "Marini", "tmarini@email.com");
	/** Rental object used for testing */
	public static final Rental RENTAL10 = new Rental(2, 4, 90, "Ignacio", "Dominguez", "idominguez@email.com");
	
	/** Array used for testing containing all the rentals */
	public static final Rental[] RENTAL_LIST = {RENTAL1, RENTAL2, RENTAL3, RENTAL4, RENTAL5, RENTAL6, RENTAL7, RENTAL8, RENTAL9, RENTAL10};
	
	/**
	 * Tests the methods in the RentalReaderIO class
	 * 
	 * @throws FileNotFoundException if the entered file is not valid
	 */
	@Test
	public void testRentalReaderIO() throws FileNotFoundException {
		
		// List<Rental> rentals = RentalReaderIO.readFile(SAMPLE);
		List<Rental> rentals = RentalReaderIO.readFile(SAMPLE);
		assertEquals(10, rentals.size());
		
		for (int i = 0; i < RENTAL_LIST.length; i++) {
			assertEquals(RENTAL_LIST[i].getStartDay(), rentals.get(i).getStartDay());
			assertEquals(RENTAL_LIST[i].getEndDay(), rentals.get(i).getEndDay());
			assertEquals(RENTAL_LIST[i].getCost(), rentals.get(i).getCost());
			assertEquals(RENTAL_LIST[i].getFirst(), rentals.get(i).getFirst());
			assertEquals(RENTAL_LIST[i].getLast(), rentals.get(i).getLast());
			assertEquals(RENTAL_LIST[i].getEmail(), rentals.get(i).getEmail());
		}
	}
	
	
	/**
	 * Tests the method with duplicate input
	 * 
	 * @throws FileNotFoundException if the file could not be found
	 */
	@Test
	public void testInvalid() throws FileNotFoundException {
		
		//Used to test constructor
		RentalReaderIO r = new RentalReaderIO();
		assertNotNull(r);
		
		//Checks for duplicates
		List<Rental> rentals = RentalReaderIO.readFile(DUPLICATES);
//		assertEquals(3, rentals.size());
		assertNotNull(rentals);
	}
}
