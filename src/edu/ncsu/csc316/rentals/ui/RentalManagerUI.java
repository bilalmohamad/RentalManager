package edu.ncsu.csc316.rentals.ui;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.Scanner;

import edu.ncsu.csc316.rentals.manager.RentalManager;

/**
 * This class is used to create the user interface for the RentalManager program
 *
 * @author Bilal Mohamad (bmohama)
 * @author Marwah Mahate (msmahate)
 *
 */
public class RentalManagerUI {

	/** Instance of the Rental Manager **/
	private RentalManager manager;
	
	/**
	 * The constructor used for the Rental Manager user interface.
	 * Prompts the user with all the available menu options
	 * 
	 * @param console	the Scanner asking for user input
	 * @param fileName	the input file name
	 * 
	 * @throws FileNotFoundException if the input file could not be found
	 */
	public RentalManagerUI(Scanner console, String fileName) throws FileNotFoundException {
		
		manager = new RentalManager(fileName);
		boolean isQuit = false;
		
		while (!isQuit) {
			prompt();
			String answer = console.next().toLowerCase();
			
			if (answer.equals("q")) {
				System.out.println("Thank you, have a nice day!");
				isQuit = true;
			}
			
			else if (answer.equals("c")) {
				System.out.println("Enter the starting day: ");
				int startDay = console.nextInt();
				System.out.println("Enter the ending day: ");
				int endDay = console.nextInt();
				System.out.println(manager.getRentals(startDay, endDay));
			}
			
			else if (answer.equals("r")) {
				System.out.println("Enter the starting day: ");
				int startDay = console.nextInt();
				System.out.println(manager.getRentalsForDay(startDay));
			}
		}
		
		console.close();
	}
	
	
	/**
	 * Static method used for recreating the menu prompt
	 */
	private static void prompt() {
		System.out.println();
		System.out.println("Welcome to the Rental Manager!");
		System.out.println("To select an option, type the first letter of any of the listed options:");
		System.out.println("(C)heapest Rental Sequence");
		System.out.println("(R)entals for a Specific Day");
		System.out.println("(Q)uit\n");
	}
	
	
	/**
	 * Initiates the program by prompting the user for the input file.
	 * Continues the program in the RentalManagerUI constructor
	 * 
	 * @param args	the arguments entered by the user
	 * 
	 * @throws FileNotFoundException if the input file does not exist
	 */
	public static void main(String[] args) throws FileNotFoundException {
		
		Scanner console = new Scanner(System.in);
		
		System.out.println("Please enter the input file: ");
		String fileName = "input/" + console.next();
		File input = new File(fileName);
		
		while(!input.exists()) {
			System.out.println("This is not a valid file. Try again\n");
			fileName = "input/" + console.next();
			input = new File(fileName);
		}
		
		new RentalManagerUI(console, fileName);
	}
}
