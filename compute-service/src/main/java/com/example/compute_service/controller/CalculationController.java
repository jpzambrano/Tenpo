package com.example.compute_service.controller;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.service.CalculateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;



@RestController
@RequestMapping("/calculation")
public class CalculationController {
    private final CalculateService calculateService;

    
    public CalculationController(CalculateService calculateService) {
        this.calculateService = calculateService;
    }

     @Operation(summary = "Perform a calculation", description = "Calculate the sum and apply a dynamic percentage.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Calculation successful"),
            @ApiResponse(responseCode = "400", description = "Invalid input provided"),
            @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    @PostMapping
    public CalculationResponse calculate(@RequestBody CalculationRequest request) {
        return calculateService.execute(request);
    }
}

