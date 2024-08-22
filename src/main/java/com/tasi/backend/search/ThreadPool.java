
package com.tasi.backend.search;

import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Thread Pool instanciator and manager.
 */
public class ThreadPool {

    /** Logger. */
    private static final Logger LOGGER = LoggerFactory.getLogger(ThreadPool.class);
    /** Single Thread Pool manager instance. */
    private static ThreadPool instance;
    /** Executor Service. */
    private ExecutorService executor;

    /**
     * Thread Pool instanciator and manager.
     */
    private ThreadPool() {
        // Creates a pool of threads
        executor = Executors.newFixedThreadPool(200);
    }

    /**
     * Runs a task in a thread.
     * @param task Task to run.
     */
    public void run(Runnable task) {
        this.executor.submit(task);
    }

    /**
     * Runs a function.
     * @param function Function to run.
     */
    public void run(Callable<?> function) {
        this.executor.submit(function);
    }

    public void shutdown() {
        executor.shutdown();
        try {
            if (!executor.awaitTermination(60, TimeUnit.SECONDS)) {
                executor.shutdownNow();
            }
        } catch (InterruptedException e) {
            LOGGER.error(e.getMessage());
            executor.shutdownNow();
            Thread.currentThread().interrupt();
        }
    }

    /**
     * Get the single instance of the Thread Pool.
     * @return the instance of the Thread Pool.
     */
    public static ThreadPool getInstance() {
        if (null == ThreadPool.instance) {
            ThreadPool.instance = new ThreadPool();
        }
        return ThreadPool.instance;
    }
}
