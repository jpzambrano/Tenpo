package com.example.compute_service.model;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;

@Schema(description = "Request payload for performing a calculation")
public record CalculationRequest( @Schema(description = "First number", example = "10") @NotNull(message = "num1 cannot be null") double num1,
        @Schema(description = "Second number", example = "20") @NotNull(message = "num2 cannot be null") double num2) 
        {}
