package com.example.compute_service.model;
import java.util.List;

public record HistoryResponse(
    List<?> content,
    int totalPages,
    long totalElements,
    int pageNumber,
    int pageSize
) {}
