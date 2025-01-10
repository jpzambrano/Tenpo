package com.example.compute_service.provider;

import java.util.Optional;
import org.springframework.stereotype.Component;



@Component
public class DefaultPercentageProvider implements PercentageProvider {

    private final ExternalServiceClient externalServiceClient;

  
    public DefaultPercentageProvider(ExternalServiceClient externalServiceClient) {
        this.externalServiceClient = externalServiceClient;
    }

    @Override
    public double getPercentage() {
        return Optional.ofNullable(externalServiceClient.getDynamicPercentage())
                       .map(response -> response.percentage())
                       .orElseThrow(() -> new IllegalStateException("Percentage not available"));
    }
}
