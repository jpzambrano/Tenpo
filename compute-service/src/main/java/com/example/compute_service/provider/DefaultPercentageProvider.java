package com.example.compute_service.provider;

import java.util.Optional;
import java.util.concurrent.TimeUnit;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.springframework.stereotype.Component;

import com.example.compute_service.cache.PercentageCache;
import com.example.compute_service.exception.PercentageNotAvailableException;

    @Component
    public class DefaultPercentageProvider implements PercentageProvider {
        private static final Logger logger = LoggerFactory.getLogger(DefaultPercentageProvider.class);

        private static final int MAX_RETRIES = 3;
        private static final long RETRY_DELAY_MS = 1000; // 1 second
    
        private final ExternalServiceClient externalServiceClient;
        private final PercentageCache cacheService;
        private final RetryService retryService;
    
        public DefaultPercentageProvider(ExternalServiceClient externalServiceClient,
                                         PercentageCache cacheService,
                                         RetryService retryService) {
            this.externalServiceClient = externalServiceClient;
            this.cacheService = cacheService;
            this.retryService = retryService;
        }
    
        @Override
        public double getPercentage() {
            logger.info("Fetching percentage value...");
            return cacheService.getLastPercentage()
                    .orElseGet(() -> {
                        logger.info("Cache miss. Fetching from external service...");
                        return fetchWithRetries();
                    });
        }
    
        private double fetchWithRetries() {
            logger.info("Fetching percentage from external service with retries...");
            return retryService.executeWithRetries(() -> {
                try {
                    var response = externalServiceClient.getDynamicPercentage();
                    if (response == null || response.percentage() <= 0) {
                        throw new PercentageNotAvailableException("Invalid percentage from external service.");
                    }
                    double percentage = response.percentage();
                    logger.info("Successfully fetched percentage: {}", percentage);
                    cacheService.cachePercentage(percentage);
                    return percentage;
                } catch (Exception e) {
                    logger.error("Error fetching percentage from external service: {}", e.getMessage());
                    throw new PercentageNotAvailableException("Failed to fetch percentage.", e);
                }
            }, MAX_RETRIES, RETRY_DELAY_MS);
        }
    }