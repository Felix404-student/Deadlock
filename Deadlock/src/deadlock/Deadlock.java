package deadlock;
import java.util.ArrayList;
import java.util.Scanner;
import java.io.*;

/**\
 * @author justinbrown
 * Inputs all data from text files
 * Tests if a complete sequence of completing processes and releasing their resources can be found
 * If not, recommend postponing process using most resources
 */
public class Deadlock {
	protected int numProjects, numResources, temp, temp2; 		// keeps track of how many rows and columns files should have
	protected ArrayList<ArrayList<Integer>> projects;	// stores all our current processes (allocated resources)
	protected ArrayList<ArrayList<Integer>> requests;	// stores all the counts of the additional resources needed to finish each process
	protected ArrayList<ArrayList<Integer>> projectSequence;	// if no deadlock exists, where we print out successful order from
	protected ArrayList<Integer> available;		// tracks currently available resources, stars with Available file
	protected ArrayList<Integer> tempArray;
	
	public void Process(String alloc, String req, String avail) {
		try {
			Scanner aScanner = new Scanner(new File(alloc));
			Scanner rScanner = new Scanner(new File(req));
			Scanner avScanner = new Scanner(new File(avail));
			
			// getting initial values for rows and columns to initialize ArrayLists
			// makes sure all files agree
			numProjects = Integer.parseInt(aScanner.nextLine());
			temp = Integer.parseInt(rScanner.nextLine());
			
			if (numProjects != temp) {
				System.out.println("Projects/Resources number mismatch.\nExiting.");
				System.exit(0);
			}
			numResources = Integer.parseInt(aScanner.nextLine());
			temp = Integer.parseInt(rScanner.nextLine());
			
			if (numResources != temp) {
				System.out.println("Projects/Resources number mismatch.\nExiting.");
				System.exit(0);
			}
			temp = Integer.parseInt(avScanner.nextLine());
			
			if (numResources != temp) {
				System.out.println("Projects/Resources number mismatch.\nExiting.");
				System.exit(0);
			}
			
			// Instantiate ArrayLists
			projects = new ArrayList<ArrayList<Integer>>(numProjects);
			projectSequence = new ArrayList<ArrayList<Integer>>(numProjects);
			requests = new ArrayList<ArrayList<Integer>>(numProjects);
			available = new ArrayList<Integer>(numResources);

			// get initial values for available resources
			String next = avScanner.nextLine();
			String datavalues[] = next.split("\t");
			
			for (int j = 0; j < numResources; j++) {
				available.add(j, Integer.parseInt(datavalues[j]));
			}
			
			// read in and store all of the currently allocated resources
			for(int i = 0; i < numProjects; i++) {
				next = aScanner.nextLine();
				tempArray = new ArrayList<Integer>(numResources);
				
				String datavalues2[] = next.split("\t");
				
				for (int j = 0; j < numResources; j++) {
					tempArray.add(j, Integer.parseInt(datavalues2[j]));
				}
				projects.add(i, tempArray);
			}
			
			// read in and store all of the additional requested resources
			for(int i = 0; i < numProjects; i++) {
				next = rScanner.nextLine();
				tempArray = new ArrayList<Integer>(numResources);
							
				String datavalues2[] = next.split("\t");
							
				for (int j = 0; j < numResources; j++) {
					tempArray.add(j, Integer.parseInt(datavalues2[j]));
				}
				requests.add(i, tempArray);
			}
			
			// attempt to process
			while(!(projects.isEmpty())) {
				int waitingOn = requests.size();			// to check for deadlock
				for (int i = 0; i < requests.size(); i++) {			// iterate over remaining jobs
					boolean canComplete = true;
					
					for (int j = 0; j < numResources; j++) {		// check if we can complete job 
						if(requests.get(i).get(j) > available.get(j))
							canComplete = false;
					}
					if(canComplete) {											// we can complete job
						tempArray = new ArrayList<Integer>(numResources);
						
						for (int j = 0; j < numResources; j++) {
							available.set(j, (available.get(j) + projects.get(i).get(j)) ); // release resources
							tempArray.add(projects.get(i).get(j) + requests.get(i).get(j));
						}
						projectSequence.add(tempArray);						// add to list of completed jobs
						requests.remove(i);									// remove job and associated request
						projects.remove(i);
					}
				}
				
				if (requests.size() == waitingOn) {			// Deadlock confirmed
					tempArray = projects.get(0);
					for (int i = 0; i < projects.size(); i++) {		// identify largest incomplete job
						temp = 0;
						temp2 = 0;
						for (int j = 0; j < numResources; j++) {
							temp += tempArray.get(j);
							temp2 += projects.get(i).get(j);
						}
						if (temp2 > temp) {
							tempArray = projects.get(i);
						}
					}
					
					System.out.println("Deadlock. Recommend postponing " + tempArray);
					System.exit(0);
				}
			}
			System.out.println("\nNo deadlock, project sequence:");
			for (ArrayList<Integer> a : projectSequence) {
				System.out.println(a);
			}
									
			/**
			 * printout for testing
			System.out.println("currently allocated:");
			for (ArrayList<Integer> a : projects) {
				System.out.println(a);
			}
			System.out.println("\nwaiting on:");
			for (ArrayList<Integer> b : requests) {
				System.out.println(b);
			}
			System.out.println("\nfree:");
			System.out.println(available);
			*/
			aScanner.close();
			rScanner.close();
			avScanner.close();
			
		} catch(Exception e) {
			System.out.println("Encountered a problem, exiting");
			e.printStackTrace();
			System.exit(0);
		}
		// printout for testing
		System.out.println("Done.");
	}
}
