package com.example.compute_service.cache;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import java.util.Optional;



import static org.junit.jupiter.api.Assertions.*;


class PercentageCacheTest {

    @Mock
    private StringRedisTemplate redisTemplate;

    @Mock
    private ValueOperations<String, String> valueOperations;

    @InjectMocks
    private PercentageCache percentageCache;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    @Test
    void testCachePercentage() {
        // Arrange
        double percentage = 0.15;

        // Act
        percentageCache.cachePercentage(percentage);

        // Assert
        verify(valueOperations, times(1)).set(eq("cached_percentage"), eq("0.15"), eq(30L), eq(java.util.concurrent.TimeUnit.MINUTES));
    }

    @Test
    void testGetLastPercentageWhenPresent() {
        // Arrange
        when(valueOperations.get("cached_percentage")).thenReturn("0.15");

        // Act
        Optional<Double> result = percentageCache.getLastPercentage();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertTrue(result.isPresent(), "Expected percentage to be present");
        assertEquals(0.15, result.get(), 0.001, "Percentage should match the cached value");
    }

    @Test
    void testGetLastPercentageWhenNotPresent() {
        // Arrange
        when(valueOperations.get("cached_percentage")).thenReturn(null);

        // Act
        Optional<Double> result = percentageCache.getLastPercentage();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isPresent(), "Expected percentage to be absent");
    }

    @Test
    void testGetLastPercentageWithInvalidValue() {
        // Arrange
        when(valueOperations.get("cached_percentage")).thenReturn("invalid");

        // Act
        Optional<Double> result = percentageCache.getLastPercentage();

        // Assert
        assertNotNull(result, "Result should not be null");
        assertFalse(result.isPresent(), "Expected percentage to be absent due to invalid value");
    }
}
