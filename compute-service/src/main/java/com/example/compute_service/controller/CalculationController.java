package com.example.compute_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.service.CalculateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/calculation")
public class CalculationController {
    private final CalculateService calculateService;

    
    public CalculationController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

    @PostMapping
    public CalculationResponse calculate(@RequestBody CalculationRequest request) {
        return calculateService.execute(request);
    }
}

