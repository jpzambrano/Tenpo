package com.example.compute_service.cache;

import java.util.Optional;
import java.util.concurrent.TimeUnit;

import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
public class PercentageCache {

    private final StringRedisTemplate redisTemplate;
    private static final String PERCENTAGE_KEY = "cached_percentage";

    public PercentageCache(StringRedisTemplate redisTemplate) {
        this.redisTemplate = redisTemplate;
    }

    /**
     * Guarda el porcentaje en caché con un tiempo de expiración.
     *
     * @param percentage El porcentaje a guardar.
     */
    public void cachePercentage(double percentage) {
        redisTemplate.opsForValue().set(PERCENTAGE_KEY, String.valueOf(percentage), 30, TimeUnit.MINUTES);
    }

    /**
     * Obtiene el porcentaje desde el caché.
     *
     * @return El porcentaje almacenado o un Optional vacío si no existe.
     */
    public Optional<Double> getLastPercentage() {
        String cachedValue = redisTemplate.opsForValue().get(PERCENTAGE_KEY);
        if (cachedValue != null) {
            try {
                return Optional.of(Double.parseDouble(cachedValue));
            } catch (NumberFormatException ex) {
                // Manejo de error si el valor en caché no es válido.
                return Optional.empty();
            }
        }
        return Optional.empty();
    }
}