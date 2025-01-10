package com.example.compute_service.provider;



import java.util.Optional;
import java.util.concurrent.Callable;
import java.util.stream.IntStream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class RetryService {

    private static final Logger logger = LoggerFactory.getLogger(RetryService.class);

    public <T> T executeWithRetries(Callable<T> task, int maxAttempts, long delayMillis) {
        return IntStream.rangeClosed(1, maxAttempts)
                        .mapToObj(attempt -> tryTaskWithDelay(task, attempt, delayMillis, maxAttempts))
                        .filter(Optional::isPresent)
                        .map(Optional::get)
                        .findFirst()
                        .orElseThrow(() -> new RuntimeException("All retry attempts failed."));
    }

    private <T> Optional<T> tryTaskWithDelay(Callable<T> task, int attempt, long delayMillis, int maxAttempts) {
        try {
            logger.info("Attempt {} of {}", attempt, maxAttempts);
            return Optional.ofNullable(task.call());
        } catch (Exception ex) {
            logger.warn("Attempt {} failed: {}", attempt, ex.getMessage());
            if (attempt < maxAttempts) {
                try {
                    Thread.sleep(delayMillis);
                } catch (InterruptedException ie) {
                    Thread.currentThread().interrupt();
                    throw new RuntimeException("Retry attempts interrupted.", ie);
                }
            }
            return Optional.empty();
        }
    }
}