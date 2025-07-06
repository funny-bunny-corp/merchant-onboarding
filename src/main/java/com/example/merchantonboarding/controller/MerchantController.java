package com.example.merchantonboarding.controller;

import com.example.merchantonboarding.controller.dto.NewMerchantRequest;
import com.example.merchantonboarding.domain.Merchant;
import com.example.merchantonboarding.domain.service.MerchantService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;

@RestController
@RequestMapping("/api/merchants")
@Tag(name = "Merchant", description = "Merchant management API")
public class MerchantController {
    
    private static final Logger logger = LoggerFactory.getLogger(MerchantController.class);
    
    private final MerchantService merchantService;
    
    @Autowired
    public MerchantController(MerchantService merchantService) {
        this.merchantService = merchantService;
    }
    
    @PostMapping
    @Operation(summary = "Create a new merchant", description = "Creates a new merchant with the provided information")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "201", description = "Merchant created successfully"),
        @ApiResponse(responseCode = "400", description = "Invalid input data")
    })
    public ResponseEntity<Void> createMerchant(@Valid @RequestBody NewMerchantRequest request) {
        logger.info("Creating new merchant with name: {}", request.getName());
        
        try {
            Merchant merchant = request.toMerchant();
            merchantService.save(merchant);
            logger.info("Merchant created successfully with ID: {}", merchant.getId());
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (Exception e) {
            logger.error("Error creating merchant: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    
    @GetMapping
    @Operation(summary = "Get all merchants", description = "Retrieves a list of all merchants")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved merchants"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CompletableFuture<ResponseEntity<List<Merchant>>> getAllMerchants() {
        logger.info("Retrieving all merchants");
        
        return merchantService.findAllMerchants()
                .thenApply(merchants -> {
                    logger.info("Found {} merchants", merchants.size());
                    return ResponseEntity.ok(merchants);
                })
                .exceptionally(ex -> {
                    logger.error("Error retrieving merchants: {}", ex.getMessage(), ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
    
    @GetMapping("/{id}")
    @Operation(summary = "Get merchant by ID", description = "Retrieves a specific merchant by their ID")
    @ApiResponses(value = {
        @ApiResponse(responseCode = "200", description = "Successfully retrieved merchant"),
        @ApiResponse(responseCode = "404", description = "Merchant not found"),
        @ApiResponse(responseCode = "500", description = "Internal server error")
    })
    public CompletableFuture<ResponseEntity<Merchant>> getMerchant(
            @Parameter(description = "Merchant ID") @PathVariable UUID id) {
        logger.info("Retrieving merchant with ID: {}", id);
        
        return merchantService.findMerchantById(id)
                .thenApply(merchant -> {
                    if (merchant != null) {
                        logger.info("Found merchant: {}", merchant.getName());
                        return ResponseEntity.ok(merchant);
                    } else {
                        logger.warn("Merchant with ID {} not found", id);
                        return ResponseEntity.notFound().build();
                    }
                })
                .exceptionally(ex -> {
                    logger.error("Error retrieving merchant with ID {}: {}", id, ex.getMessage(), ex);
                    return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
                });
    }
}