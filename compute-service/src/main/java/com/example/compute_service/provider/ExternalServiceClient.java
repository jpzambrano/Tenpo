package com.example.compute_service.provider;

import org.springframework.stereotype.Service;

@Service
public class ExternalServiceClient {

    public ExternalServiceResponse getDynamicPercentage() {
        return new ExternalServiceResponse(0.10); // Simula un porcentaje fijo del 10%
    }
}