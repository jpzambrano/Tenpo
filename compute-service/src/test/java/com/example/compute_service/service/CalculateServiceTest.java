package com.example.compute_service.service;


import static org.mockito.Mockito.*;

import com.example.compute_service.cache.PercentageCache;
import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.provider.PercentageProvider;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.junit.jupiter.api.Assertions.*;

class CalculateServiceTest {

    
    @Mock
    private PercentageProvider percentageProvider;

    @Mock
    private HistoryService historyService;

    @Mock
    private PercentageCache percentageCache;

    @InjectMocks
    private CalculateService calculateService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCalculateWithValidPercentage() {
        // Arrange
        when(percentageProvider.getPercentage()).thenReturn(0.10);
        CalculationRequest request = new CalculationRequest(10, 20);

        // Act
        CalculationResponse response = calculateService.execute(request);

        // Assert
        assertEquals(33.0, response.result(), 0.001);
        verify(historyService, times(1)).saveCalculation(10, 20, 33.0);
    }

    @Test
    void testCalculateWithCacheFallback() {
        // Arrange
        when(percentageProvider.getPercentage()).thenThrow(new RuntimeException("Service unavailable"));
        when(percentageCache.getLastPercentage()).thenReturn(java.util.Optional.of(0.05));
        CalculationRequest request = new CalculationRequest(10, 20);

        // Act
        CalculationResponse response = calculateService.execute(request);

        // Assert
        assertEquals(31.5, response.result(), 0.001);
        verify(historyService, times(1)).saveCalculation(10, 20, 31.5);
    }

    @Test
    void testCalculateWithNoCacheAvailable() {
        // Arrange
        when(percentageProvider.getPercentage()).thenThrow(new RuntimeException("Service unavailable"));
        when(percentageCache.getLastPercentage()).thenReturn(java.util.Optional.empty());
        CalculationRequest request = new CalculationRequest(10, 20);

        // Act & Assert
        RuntimeException exception = assertThrows(RuntimeException.class, () -> calculateService.execute(request));
        assertEquals("No cached percentage available.", exception.getMessage());
    }

    @Test
    void testCalculateWithInvalidInputs() {
        // Arrange
        CalculationRequest request = new CalculationRequest(-10, 20);

        // Act & Assert
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> calculateService.execute(request));
        assertEquals("num1 and num2 must be non-negative.", exception.getMessage());
    }
}