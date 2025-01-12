package com.example.compute_service.model;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Request payload for performing a calculation")
public record CalculationRequest( @Schema(description = "First number", example = "10") double num1,
        @Schema(description = "Second number", example = "20") double num2) {}
