package com.example.compute_service.model;

import java.time.LocalDateTime;

public record HistoryResponse(
    double num1,
    double num2,
    double result,
    LocalDateTime timestamp
) {}
