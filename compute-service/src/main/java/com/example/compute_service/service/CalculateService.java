package com.example.compute_service.service;

import java.util.stream.Stream;

import org.springframework.stereotype.Service;

import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.provider.PercentageProvider;

@Service
public class CalculateService {

    private final PercentageProvider percentageProvider;

    
    public CalculateService(PercentageProvider percentageProvider) {
        this.percentageProvider = percentageProvider;
    }

    public CalculationResponse execute(CalculationRequest request) {
        double sum = Stream.of(request.num1(), request.num2())
                           .reduce(0.0, Double::sum); // Suma los números

        double percentage = percentageProvider.getPercentage();
        double result = sum + (sum * percentage);

        return new CalculationResponse(result);
    }
}