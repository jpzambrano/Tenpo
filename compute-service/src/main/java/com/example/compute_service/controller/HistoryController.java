package com.example.compute_service.controller;

import com.example.compute_service.model.HistoryResponse;
import com.example.compute_service.service.HistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;

import org.springframework.data.domain.Page;
import org.springframework.data.web.PagedResourcesAssembler;
import org.springframework.hateoas.EntityModel;
import org.springframework.hateoas.PagedModel;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HistoryController {

    private final HistoryService historyService;

    
    public HistoryController(HistoryService historyService) {
        this.historyService = historyService;
    }
    @Operation(summary = "Get calculation history", description = "Retrieve the history of calculations with pagination.")
@ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "History retrieved successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid pagination parameters"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
})

     @GetMapping("/api/history")
    public PagedModel<EntityModel<HistoryResponse>> getHistory(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            PagedResourcesAssembler<HistoryResponse> assembler) {
        Page<HistoryResponse> historyPage = historyService.getHistory(page, size);
        return assembler.toModel(historyPage);
    }
}
