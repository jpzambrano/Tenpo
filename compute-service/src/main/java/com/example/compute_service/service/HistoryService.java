package com.example.compute_service.service;

import com.example.compute_service.entity.CalculationHistory;
import com.example.compute_service.model.HistoryResponse;
import com.example.compute_service.repository.HistoryRepository;

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

    public Page<HistoryResponse> getHistory(int page, int size) {
        return historyRepository.findAll(PageRequest.of(page, size))
                                .map(history -> new HistoryResponse(
                                    history.getNum1(),
                                    history.getNum2(),
                                    history.getResult(),
                                    history.getTimestamp()
                                ));
    }
}
