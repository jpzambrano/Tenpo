package com.example.compute_service.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import com.example.compute_service.cache.PercentageCache;
import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.provider.PercentageProvider;

@Service
public class CalculateService {

  
    private final PercentageProvider percentageProvider;
    private final HistoryService historyService;
    private final PercentageCache cacheService; 
    private final Logger logger = LoggerFactory.getLogger(CalculateService.class);

    public CalculateService(PercentageProvider percentageProvider, 
                            HistoryService historyService, 
                            PercentageCache cacheService) {
        this.percentageProvider = percentageProvider;
        this.historyService = historyService;
        this.cacheService = cacheService;
    }

    public CalculationResponse execute(CalculationRequest request) {
        validateRequest(request);

        // Intentar obtener el porcentaje con resiliencia.
        double percentage = fetchPercentage();

        // Calcular el resultado.
        double sum = Stream.of(request.num1(), request.num2())
                           .reduce(0.0, Double::sum);
        double result = percentage * sum + sum;

        // Guardar en historial si el cálculo fue exitoso.
        historyService.saveCalculation(request.num1(), request.num2(), result);
        
        logger.info("Calculation completed: num1={}, num2={}, result={}", 
                     request.num1(), request.num2(), result);

        return new CalculationResponse(result);
    }

    private double fetchPercentage() {
        try {
            return percentageProvider.getPercentage();
        } catch (Exception ex) {
            logger.warn("Error fetching percentage from provider: {}. Falling back to cache.", ex.getMessage());
            return cacheService.getLastPercentage()
                               .orElseThrow(() -> new RuntimeException("No cached percentage available."));
        }
    }

    private void validateRequest(CalculationRequest request) {
        if (Double.isNaN(request.num1()) || Double.isNaN(request.num2())) {
            throw new IllegalArgumentException("num1 and num2 cannot be null.");
        }
        if (request.num1() < 0 || request.num2() < 0) {
            throw new IllegalArgumentException("num1 and num2 must be non-negative.");
        }
    }
}