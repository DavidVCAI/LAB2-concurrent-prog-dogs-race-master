package edu.eci.arsw.primefinder;

import java.util.LinkedList;
import java.util.List;

/**
 * Thread implementation for finding prime numbers within a specified range.
 * This class extends Thread and calculates all prime numbers between two given integers.
 * Supports pause and resume functionality through a ThreadController.
 *
 * @author Jesús Pinzón & David Velásquez
 * @version 1.0
 * @since 2025-08-28
 */
public class PrimeFinderThread extends Thread {

    private int threadId;
    private int startRange;
    private int endRange;
    private List<Integer> primes = new LinkedList<Integer>();
    private ThreadController controller;

    /**
     * Constructor to initialize the prime finder thread with a specific range and controller.
     *
     * @param threadId the custom identifier for this thread
     * @param startRange the starting value of the range (inclusive)
     * @param endRange the ending value of the range (inclusive)
     * @param controller the thread controller for pause/resume functionality
     */
    public PrimeFinderThread(int threadId, int startRange, int endRange, ThreadController controller) {
        super();
        this.threadId = threadId;
        this.startRange = startRange;
        this.endRange = endRange;
        this.controller = controller;
    }

    /**
     * Main execution method of the thread. Iterates through the specified range and finds all prime numbers.
     * Checks for pause points periodically to support pause/resume functionality.
     * Each found prime number is printed to the console.
     */
    @Override
    public void run() {
        try {
            for (int i = startRange; i <= endRange; i++) {
                // Check pause point every 1000 iterations for better responsiveness
                if (i % 1000 == 0) {
                    controller.checkPausePoint();
                }

                if (isPrime(i)) {
                    primes.add(i);
                    System.out.println("Thread " + threadId + " found prime: " + i);
                }
            }
        } catch (InterruptedException e) {
            System.out.println("Thread " + threadId + " was interrupted");
            Thread.currentThread().interrupt();
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

    /**
     * Returns the custom thread identifier.
     * @return the thread ID assigned to this thread
     */
    public int getThreadId() {
        return threadId;
    }
}
