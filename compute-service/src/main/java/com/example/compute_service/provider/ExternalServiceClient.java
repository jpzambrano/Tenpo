package com.example.compute_service.provider;

import java.util.Optional;
import java.util.Random;

import org.springframework.stereotype.Service;

@Service
public class ExternalServiceClient {

   private final Random random = new Random();

    public ExternalServiceResponse getDynamicPercentage() {
        return Optional.ofNullable(random.nextInt(3)) // Generate a random number
                       .filter(val -> val != 0) // Fail 1 out of 3 attempts
                       .map(val -> new ExternalServiceResponse(0.10)) // Provide a fixed percentage
                       .orElse(null); // Simulate failure
    }

    public record ExternalServiceResponse(double percentage) {}
}