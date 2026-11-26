package com.expensetracker;
import com.expensetracker.concurrent.ConcurrentTaskProcessor;
import org.junit.jupiter.api.Test;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;
import static org.junit.jupiter.api.Assertions.*;
class ConcurrentTaskProcessorTest {
    @Test
    void processesTasksSafelyUsingWorkerThreadAndQueue() throws Exception {
        int numberOfTasks = 100;
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch latch = new CountDownLatch(numberOfTasks);
        ConcurrentTaskProcessor processor =
                new ConcurrentTaskProcessor();
        try {
            for (int i = 0; i < numberOfTasks; i++) {
                processor.submit(() -> {
                    counter.incrementAndGet();
                    latch.countDown();
                });
            }
            assertTrue(latch.await(5, java.util.concurrent.TimeUnit.SECONDS));
        } finally {
            processor.close();
        }
        assertEquals(numberOfTasks, counter.get());
        assertEquals(numberOfTasks, processor.getCompletedTasks());
    }
}
