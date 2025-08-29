package edu.eci.arsw.primefinder;

import java.io.IOException;
import java.util.Timer;
import java.util.TimerTask;

/**
 * Main class to execute the prime number finder using multiple threads with pause/resume functionality.
 * This implementation divides the work of finding prime numbers between 0 and 30,000,000
 * among three separate threads, automatically pauses every 5 seconds, and resumes when ENTER is pressed.
 *
 * @author Jesús Pinzón & David Velásquez
 * @version 1.0
 * @since 2025-08-22
 */
public class Main {

    private static final int MAX_VALUE = 30000000;
    private static final int THREAD_COUNT = 3;
    private static final int SEPARATORS_NUMBER = 65;
    private static final int PAUSE_DELAY_MS = 3000; // 5 seconds

    private static ThreadController controller;
    private static PrimeFinderThread[] threads;
    private static Timer currentTimer;
    private static boolean executionCompleted = false;

    /**
     * Main method that creates and executes three threads to find prime numbers
     * in parallel across different ranges with automatic pause every 5 seconds and manual resume functionality.
     *
     * @param args command line arguments (not used)
     */
    public static void main(String[] args) {
        System.out.println("=".repeat(SEPARATORS_NUMBER));
        System.out.println("Starting prime number search from 0 to " + MAX_VALUE + " using " + THREAD_COUNT + " threads...");
        System.out.println("Automatic pause every 5 seconds - Press ENTER to resume");
        System.out.println("=".repeat(SEPARATORS_NUMBER));

        // Create shared thread controller
        controller = new ThreadController();

        // Calculate range size for each thread
        int rangeSize = MAX_VALUE / THREAD_COUNT;

        // Create array to hold thread references
        threads = new PrimeFinderThread[THREAD_COUNT];

        // Record start time for performance measurement
        long startTime = System.currentTimeMillis();

        // Create and start threads with specific ranges
        for (int i = 0; i < THREAD_COUNT; i++) {
            int startRange = i * rangeSize;
            int endRange = (i == THREAD_COUNT - 1) ? MAX_VALUE : (i + 1) * rangeSize - 1;

            threads[i] = new PrimeFinderThread(i + 1, startRange, endRange, controller);
            System.out.println("Thread " + (i + 1) + " will search range: " + startRange + " to " + endRange);
            threads[i].start();
        }

        System.out.println("=".repeat(SEPARATORS_NUMBER));
        System.out.println("All threads started. Searching for primes...");
        System.out.println("=".repeat(SEPARATORS_NUMBER));

        // Start the recursive timer
        scheduleNextPause();

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

        // Mark execution as completed and cancel any pending timers
        executionCompleted = true;
        if (currentTimer != null) {
            currentTimer.cancel();
        }

        // Calculate total execution time
        long endTime = System.currentTimeMillis();
        long executionTime = endTime - startTime;

        // Calculate and display final results
        displayFinalResults(threads, executionTime);
    }

    /**
     * Schedules the next pause timer. This method is called recursively
     * to create a timer every 5 seconds until execution is completed.
     */
    private static void scheduleNextPause() {
        if (executionCompleted) {
            return;
        }

        currentTimer = new Timer();
        currentTimer.schedule(new TimerTask() {
            @Override
            public void run() {
                if (executionCompleted) {
                    currentTimer.cancel();
                    return;
                }

                // Check if any thread is still alive before pausing
                boolean anyThreadAlive = false;
                for (PrimeFinderThread thread : threads) {
                    if (thread.isAlive()) {
                        anyThreadAlive = true;
                        break;
                    }
                }

                if (!anyThreadAlive) {
                    currentTimer.cancel();
                    return;
                }

                controller.pause();
                System.out.println("\n" + "=".repeat(SEPARATORS_NUMBER));
                System.out.println("⏸️  THREADS PAUSED AFTER 5 SECONDS");
                System.out.println("=".repeat(SEPARATORS_NUMBER));

                // Display current progress
                displayCurrentProgress(threads);

                System.out.println("Press ENTER to resume execution...");
                System.out.println("=".repeat(SEPARATORS_NUMBER));

                // Wait for ENTER key press in a separate thread
                new Thread(() -> {
                    try {
                        System.in.read(); // Wait for ENTER

                        if (!executionCompleted) {
                            controller.resume();
                            System.out.println("\n" + "=".repeat(SEPARATORS_NUMBER));
                            System.out.println("▶️  THREADS RESUMED - Continuing prime search...");
                            System.out.println("Next pause in 5 seconds - Press ENTER to resume when paused");
                            System.out.println("=".repeat(SEPARATORS_NUMBER));

                            // Schedule the next pause
                            scheduleNextPause();
                        }
                    } catch (IOException e) {
                        System.err.println("Error reading input: " + e.getMessage());
                    }
                }).start();

                currentTimer.cancel(); // Cancel current timer
            }
        }, PAUSE_DELAY_MS);
    }

    /**
     * Displays the current progress of all threads when paused.
     *
     * @param threads array of PrimeFinderThread instances
     */
    private static void displayCurrentProgress(PrimeFinderThread[] threads) {
        System.out.println("📊 CURRENT PROGRESS:");
        int totalPrimesFound = 0;

        for (PrimeFinderThread thread : threads) {
            int primesFound = thread.getPrimesCount();
            totalPrimesFound += primesFound;
            System.out.println("   Thread " + thread.getThreadId() + " - Primes found so far: " + primesFound);
        }

        System.out.println("   Total primes found so far: " + totalPrimesFound);
    }

    /**
     * Displays the final execution results.
     *
     * @param threads array of PrimeFinderThread instances
     * @param executionTime total execution time in milliseconds
     */
    private static void displayFinalResults(PrimeFinderThread[] threads, long executionTime) {
        // Calculate total primes found
        int totalPrimes = 0;
        for (PrimeFinderThread thread : threads) {
            totalPrimes += thread.getPrimesCount();
        }

        // Display final results
        System.out.println("=".repeat(SEPARATORS_NUMBER));
        System.out.println("✅ EXECUTION COMPLETED");
        System.out.println("=".repeat(SEPARATORS_NUMBER));
        System.out.println("Total prime numbers found: " + totalPrimes);
        System.out.println("Execution time: " + executionTime + " milliseconds");
        System.out.println("Range searched: 0 to " + MAX_VALUE);
        System.out.println("Number of threads used: " + THREAD_COUNT);
        System.out.println("=".repeat(SEPARATORS_NUMBER));
    }
}
