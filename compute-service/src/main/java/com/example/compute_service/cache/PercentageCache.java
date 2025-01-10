package com.example.compute_service.cache;

import java.util.Map;
import java.util.Optional;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;

import org.springframework.stereotype.Component;

@Component
public class PercentageCache {

  
    private final Map<String, CacheEntry> cache = new ConcurrentHashMap<>();

    public void put(String key, double value, long ttl, TimeUnit timeUnit) {
        long expiryTime = System.currentTimeMillis() + timeUnit.toMillis(ttl);
        cache.put(key, new CacheEntry(value, expiryTime));
    }

    public Double get(String key) {
        cleanUp();
        return Optional.ofNullable(cache.get(key))
                       .map(CacheEntry::value)
                       .orElse(null);
    }

    private void cleanUp() {
        cache.entrySet().removeIf(entry -> !entry.getValue().isValid());
    }

    private record CacheEntry(double value, long expiryTime) {
        public boolean isValid() {
            return System.currentTimeMillis() < expiryTime;
        }
    }
}