package edu.ncsu.csc316.rentals.data;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import org.junit.Test;

import edu.ncsu.csc316.rentals.factory.DSAFactory;
import edu.ncsu.csc316.rentals.data.Rental;

/**
 * Tests one rental.
 * 
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class RentalTest {
	
	/**
	 * Tests one rental.
	 */
	@Test
	public void testRentals() {
		Rental r1 = new Rental(1, 4, 85, "Jason", "King", "jtking@email.com");
		
		assertEquals(1, r1.getStartDay());
		assertEquals(4, r1.getEndDay());
		assertEquals(85, r1.getCost());
		assertEquals(85, r1.getWeight());
		assertEquals("Jason", r1.getFirst());
		assertEquals("King", r1.getLast());
		assertEquals("jtking@email.com", r1.getEmail());
		
		assertNotNull(r1.toString());
		assertEquals(true, r1.equals(r1));
		assertEquals(false, r1 == null);
//		assertEquals(false, r1.equals("hi"));
		
		DSAFactory d = new DSAFactory();
		assertNotNull(d);
	}
}
