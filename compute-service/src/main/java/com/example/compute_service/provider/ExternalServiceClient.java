package com.example.compute_service.provider;

import org.springframework.stereotype.Service;

@Service
public class ExternalServiceClient {

    public ExternalServiceResponse getDynamicPercentage() {
        return new ExternalServiceResponse(0.10); // Simulates a fixed percentage of 10%
    }

    public record ExternalServiceResponse(double percentage) {}
}