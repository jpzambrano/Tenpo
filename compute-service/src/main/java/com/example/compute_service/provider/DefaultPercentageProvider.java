package com.example.compute_service.provider;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

import com.example.compute_service.cache.PercentageCache;
import com.example.compute_service.exception.PercentageNotAvailableException;

    @Component
    public class DefaultPercentageProvider implements PercentageProvider {
    
        private static final String CACHE_KEY = "percentage";
        private static final long CACHE_TTL = 30; // 30 minutes
        private static final int MAX_RETRIES = 3;
        private static final long RETRY_DELAY_MS = 1000; // 1 second
    
        private final ExternalServiceClient externalServiceClient;
        private final PercentageCache percentageCache;
        private final RetryService retryService;
    
    
        public DefaultPercentageProvider(ExternalServiceClient externalServiceClient,
                                         PercentageCache percentageCache,
                                         RetryService retryService) {
            this.externalServiceClient = externalServiceClient;
            this.percentageCache = percentageCache;
            this.retryService = retryService;
        }
    
        @Override
        public double getPercentage() {
            return Optional.ofNullable(percentageCache.get(CACHE_KEY))
                           .orElseGet(this::fetchWithRetries);
        }
    
        private double fetchWithRetries() {
            return retryService.executeWithRetries(() -> {
                var response = externalServiceClient.getDynamicPercentage();
                return Optional.ofNullable(response)
                               .map(res -> {
                                   double percentage = res.percentage();
                                   percentageCache.put(CACHE_KEY, percentage, CACHE_TTL, TimeUnit.MINUTES);
                                   return percentage;
                               })
                               .orElseThrow(() -> new PercentageNotAvailableException("External service returned null."));
            }, MAX_RETRIES, RETRY_DELAY_MS);
        }
    }
