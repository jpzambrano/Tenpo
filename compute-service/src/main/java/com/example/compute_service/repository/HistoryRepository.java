package com.example.compute_service.repository;

import com.example.compute_service.entity.CalculationHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface HistoryRepository extends JpaRepository<CalculationHistory, Long> {
}
