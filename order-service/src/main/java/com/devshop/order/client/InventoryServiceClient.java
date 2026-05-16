package com.devshop.order.client;

import com.devshop.common.dto.ApiResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;

/**
 * Feign client for calling Inventory Service
 * Integrated with Resilience4j circuit breaker via configuration
 */
@FeignClient(
    name = "inventory-service",
    url = "${inventory.service.url:http://localhost:8081}"
)
public interface InventoryServiceClient {
    
    /**
     * Deduct stock from inventory
     * Falls back if inventory service is unavailable (circuit breaker)
     */
    @PostMapping("/api/v1/inventory/products/{id}/deduct")
    ApiResponse<?> deductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestHeader("X-Store-Id") Long storeId
    );
}

