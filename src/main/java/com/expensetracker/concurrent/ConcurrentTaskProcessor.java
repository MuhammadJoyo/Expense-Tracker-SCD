package com.expensetracker.concurrent;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.ReentrantLock;
/**
 * Demonstrates concurrency, message passing, and thread safety.
 */
public class ConcurrentTaskProcessor implements AutoCloseable {
    private final BlockingQueue<Runnable> queue =
            new LinkedBlockingQueue<>();
    private final ReentrantLock lock = new ReentrantLock();
    private final Thread worker;
    private volatile boolean running = true;
    private long completedTasks = 0;
    public ConcurrentTaskProcessor() {
        worker = new Thread(this::processTasks, "expense-worker");
        worker.start();
    }
    public void submit(Runnable task) {
        if (!running) {
            throw new IllegalStateException("Processor is closed");
        }
        queue.offer(task);
    }
    private void processTasks() {
        while (running || !queue.isEmpty()) {
            try {
                Runnable task = queue.poll(100, TimeUnit.MILLISECONDS);
                if (task != null) {
                    task.run();
                    lock.lock();
                    try {
                        completedTasks++;
                    } finally {
                        lock.unlock();
                    }
                }
            } catch (InterruptedException e) {
                // Recheck running status and continue processing queued tasks.
            }
        }
    }
    public long getCompletedTasks() {
        lock.lock();
        try {
            return completedTasks;
        } finally {
            lock.unlock();
        }
    }
    @Override
    public void close() {
        running = false;
        worker.interrupt();
        try {
            worker.join(5000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
