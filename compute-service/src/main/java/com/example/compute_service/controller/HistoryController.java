package com.example.compute_service.controller;

import com.example.compute_service.model.HistoryResponse;
import com.example.compute_service.service.HistoryService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import reactor.core.publisher.Mono;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping ("/api")    
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

@GetMapping("/history")
public Mono<HistoryResponse> getHistory(@RequestParam("page") int page, @RequestParam("size") int size) {
    return historyService.getHistory(page, size)
            .map(pagedData -> new HistoryResponse(
                    pagedData.getContent(),
                    pagedData.getTotalPages(),
                    pagedData.getTotalElements(),
                    page,
                    size
            ));
}

}