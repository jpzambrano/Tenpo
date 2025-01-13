package com.example.compute_service.service;

import com.example.compute_service.entity.CalculationHistory;
import com.example.compute_service.repository.HistoryRepository;

import reactor.core.publisher.Mono;

import java.time.LocalDateTime;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;

@Service
public class HistoryService {

    private final HistoryRepository historyRepository;

    public HistoryService(HistoryRepository historyRepository) {
        this.historyRepository = historyRepository;
    }

    public void saveCalculation(double num1, double num2, double result) {
        CalculationHistory history = new CalculationHistory();
        history.setNum1(num1);
        history.setNum2(num2);
        history.setResult(result);
        history.setTimestamp(LocalDateTime.now());
        historyRepository.save(history);
    }

 public Mono<Page<CalculationHistory>> getHistory(int page, int size) {
        return Mono.fromCallable(() -> {
            PageRequest pageable = PageRequest.of(page, size);
            return historyRepository.findAll(pageable);
        });
    }
}
