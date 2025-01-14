package com.example.compute_service.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.compute_service.model.CalculationRequest;
import com.example.compute_service.model.CalculationResponse;
import com.example.compute_service.service.CalculateService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RestController
@RequestMapping("/api")
@Validated
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
    @PostMapping("/calculate")    
    public ResponseEntity<CalculationResponse> calculate(@Valid @RequestBody CalculationRequest request) {
        CalculationResponse response = calculateService.execute(request);
        return ResponseEntity.ok(response);
        
    }
}
