package com.example.compute_service.cache;

import java.util.Map;
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
        CacheEntry entry = cache.get(key); // Obtener la entrada del caché
        return (entry != null) ? entry.value() : null; // Retornar el valor o null si no existe
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