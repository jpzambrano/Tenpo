package com.example.compute_service.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.time.LocalDateTime;
import java.util.List;

import org.springframework.data.domain.Page;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.example.compute_service.entity.CalculationHistory;
import com.example.compute_service.repository.HistoryRepository;

public class HistoryServiceTest {
     @Mock
    private HistoryRepository historyRepository;

    @InjectMocks
    private HistoryService historyService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCalculation() {
        // Arrange
        double num1 = 10.0;
        double num2 = 20.0;
        double result = 30.0;
        CalculationHistory history = new CalculationHistory();
        history.setNum1(num1);
        history.setNum2(num2);
        history.setResult(result);
        history.setTimestamp(LocalDateTime.now());

        // Act
        historyService.saveCalculation(num1, num2, result);

        // Assert
        verify(historyRepository, times(1)).save(any(CalculationHistory.class));
    }
    @Test
    void testGetHistory() {
        // Arrange
        int page = 0;
        int size = 2;
        List<CalculationHistory> historyList = List.of(
            new CalculationHistory() {{ setId(1L); setNum1(10.0); setNum2(20.0); setResult(30.0); setTimestamp(LocalDateTime.now()); }},
            new CalculationHistory() {{ setId(2L); setNum1(15.0); setNum2(25.0); setResult(40.0); setTimestamp(LocalDateTime.now()); }}
        );
        Page<CalculationHistory> historyPage = new PageImpl<>(historyList, PageRequest.of(page, size), historyList.size());

        when(historyRepository.findAll(any(PageRequest.class))).thenReturn(historyPage);

        // Act
        var result = historyService.getHistory(page, size).block();

        // Assert
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(2, result.getContent().size());
    }
}
