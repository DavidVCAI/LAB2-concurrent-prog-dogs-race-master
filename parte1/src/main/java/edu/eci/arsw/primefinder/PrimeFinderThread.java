package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

/**
 * Thread implementation for finding prime numbers within a specified range.
 * This class extends Thread and calculates all prime numbers between two given integers.
 *
 * @author Jesús Pinzón & David Velásquez
 * @version 1.0
 */
public class PrimeFinderThread extends Thread {

	/**
	 * Starting value of the range (inclusive)
	 */
	private int startRange;

	/**
	 * Ending value of the range (inclusive)
	 */
	private int endRange;

	/**
	 * List to store all prime numbers found within the specified range
	 */
	private List<Integer> primes = new LinkedList<Integer>();

	/**
	 * Constructor to initialize the prime finder thread with a specific range.
	 *
	 * @param startRange the starting value of the range (inclusive)
	 * @param endRange the ending value of the range (inclusive)
	 */
	public PrimeFinderThread(int startRange, int endRange) {
		super();
		this.startRange = startRange;
		this.endRange = endRange;
	}

	/**
	 * Main execution method of the thread. Iterates through the specified range
	 * and finds all prime numbers, storing them in the primes list.
	 * Each found prime number is printed to the console.
	 */
	@Override
	public void run() {
		for (int i = startRange; i <= endRange; i++) {
			if (isPrime(i)) {
				primes.add(i);
				System.out.println("Thread " + Thread.currentThread().getId() + " found prime: " + i);
			}
		}
	}

	/**
	 * Determines if a given number is prime.
	 * A prime number is a natural number greater than 1 that has no positive divisors other than 1 and itself.
	 *
	 * @param number the number to check for primality
	 * @return true if the number is prime, false otherwise
	 */
	private boolean isPrime(int number) {
		if (number <= 1) {
			return false;
		}
		if (number == 2) {
			return true;
		}
		if (number % 2 == 0) {
			return false;
		}

		for (int i = 3; i * i <= number; i += 2) {
			if (number % i == 0) {
				return false;
			}
		}
		return true;
	}

	/**
	 * Returns the list of prime numbers found by this thread.
	 * @return a list containing all prime numbers found within the specified range
	 */
	public List<Integer> getPrimes() {
		return primes;
	}

	/**
	 * Returns the total count of prime numbers found by this thread.
	 * @return the number of prime numbers found
	 */
	public int getPrimesCount() {
		return primes.size();
	}
}
