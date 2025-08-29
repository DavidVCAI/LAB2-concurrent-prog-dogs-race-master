package edu.eci.arsw.primefinder;

/**
 * Controller class for managing thread execution state.
 * Provides synchronization mechanisms to pause and resume multiple threads simultaneously.
 * Uses wait/notify pattern for efficient thread coordination.
 *
 * @author Jesús Pinzón & David Velásquez
 * @version 1.2
 * @since 2025-08-22
 */
public class ThreadController {

    private boolean isPaused = false;
    private final Object monitor = new Object();

    /**
     * Pauses all threads that call checkPausePoint().
     * Threads will wait until resume() is called.
     */
    public void pause() {
        synchronized (monitor) {
            isPaused = true;
        }
    }

    /**
     * Resumes all paused threads.
     * Notifies all waiting threads to continue execution.
     */
    public void resume() {
        synchronized (monitor) {
            isPaused = false;
            monitor.notifyAll();
        }
    }

    /**
     * Check point that threads should call periodically.
     * If the controller is in paused state, the calling thread will wait until resume() is called.
     *
     * @throws InterruptedException if the thread is interrupted while waiting
     */
    public void checkPausePoint() throws InterruptedException {
        synchronized (monitor) {
            while (isPaused) {
                monitor.wait();
            }
        }
    }

    /**
     * Returns the current pause state.
     * @return true if threads are paused, false otherwise
     */
    public boolean isPaused() {
        synchronized (monitor) {
            return isPaused;
        }
    }
}
