package com.example.compute_service.service;

import java.util.Optional;
import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.provider.PercentageProvider;

@Service
public class CalculateService {

    private final PercentageProvider percentageProvider;
    private final HistoryService historyService;
    
    public CalculateService(PercentageProvider percentageProvider, HistoryService historyService) {
        this.percentageProvider = percentageProvider;
        this.historyService = historyService;
    }

    public CalculationResponse execute(CalculationRequest request) {
        double sum = Stream.of(request.num1(), request.num2())
                           .reduce(0.0, Double::sum);
    
        double result = percentageProvider.getPercentage() * sum + sum;
    
        Optional.of(result)
                .ifPresent(res -> historyService.saveCalculation(request.num1(), request.num2(), res));
    
       
        return new CalculationResponse(result);
    }
}