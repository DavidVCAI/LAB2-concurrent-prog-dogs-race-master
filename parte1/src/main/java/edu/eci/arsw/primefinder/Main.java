package edu.eci.arsw.primefinder;

/**
 * Main class to execute the prime number finder using multiple threads.
 * This implementation divides the work of finding prime numbers between 0 and 30,000,000
 * among three separate threads to improve performance and utilize multiple CPU cores.
 *
 * @author Jesús Pinzón & David Velásquez
 * @version 1.0
 */
public class Main {

	/**
	 * Maximum number to search for primes
	 */
	private static final int MAX_VALUE = 30000000;

	/**
	 * Number of threads to use for the computation
	 */
	private static final int THREAD_COUNT = 3;

	/**
	 * Main method that creates and executes three threads to find prime numbers
	 * in parallel across different ranges.
	 *
	 * @param args command line arguments (not used)
	 */
	public static void main(String[] args) {

		System.out.println("Starting prime number search from 0 to " + MAX_VALUE + " using " + THREAD_COUNT + " threads...");
		System.out.println("================================================");

		// Calculate range size for each thread
		int rangeSize = MAX_VALUE / THREAD_COUNT;

		// Create array to hold thread references
		PrimeFinderThread[] threads = new PrimeFinderThread[THREAD_COUNT];

		// Record start time for performance measurement
		long startTime = System.currentTimeMillis();

		// Create and start threads with specific ranges
		for (int i = 0; i < THREAD_COUNT; i++) {
			int startRange = i * rangeSize;
			int endRange = (i == THREAD_COUNT - 1) ? MAX_VALUE : (i + 1) * rangeSize - 1;

			threads[i] = new PrimeFinderThread(startRange, endRange);
			System.out.println("Thread " + (i + 1) + " will search range: " + startRange + " to " + endRange);
			threads[i].start();
		}

		System.out.println("================================================");
		System.out.println("All threads started. Searching for primes...");
		System.out.println("================================================");

		// Wait for all threads to complete using join()
		try {
			for (int i = 0; i < THREAD_COUNT; i++) {
				threads[i].join();
				System.out.println("Thread " + (i + 1) + " completed. Found " +
						threads[i].getPrimesCount() + " primes.");
			}
		} catch (InterruptedException e) {
			System.err.println("Main thread was interrupted while waiting for worker threads: " + e.getMessage());
			Thread.currentThread().interrupt();
			return;
		}

		// Calculate total execution time
		long endTime = System.currentTimeMillis();
		long executionTime = endTime - startTime;

		// Calculate and display results
		int totalPrimes = 0;
		for (PrimeFinderThread thread : threads) {
			totalPrimes += thread.getPrimesCount();
		}

		// Display final results
		System.out.println("================================================");
		System.out.println("EXECUTION COMPLETED");
		System.out.println("================================================");
		System.out.println("Total prime numbers found: " + totalPrimes);
		System.out.println("Execution time: " + executionTime + " milliseconds");
		System.out.println("Range searched: 0 to " + MAX_VALUE);
		System.out.println("Number of threads used: " + THREAD_COUNT);
		System.out.println("================================================");
	}
}
