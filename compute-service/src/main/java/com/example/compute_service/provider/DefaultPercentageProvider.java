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

    private final ExternalServiceClient externalServiceClient;
    private final PercentageCache percentageCache;

    
    public DefaultPercentageProvider(ExternalServiceClient externalServiceClient, PercentageCache percentageCache) {
        this.externalServiceClient = externalServiceClient;
        this.percentageCache = percentageCache;
    }

   @Override
public double getPercentage() {
    Double cachedValue = percentageCache.get(CACHE_KEY);

    if (cachedValue != null) {
        return cachedValue;
    }

    return fetchAndCachePercentage();
}

private double fetchAndCachePercentage() {
    var response = externalServiceClient.getDynamicPercentage();

    if (response != null) {
        double percentage = response.percentage();
        percentageCache.put(CACHE_KEY, percentage, CACHE_TTL, TimeUnit.MINUTES);
        return percentage;
    }

    throw new PercentageNotAvailableException("Percentage not available and no cached value found");
}
}
