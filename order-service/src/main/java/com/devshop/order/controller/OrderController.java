package com.devshop.order.controller;

import com.devshop.common.dto.ApiResponse;
import com.devshop.order.model.Order;
import com.devshop.order.service.OrderService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {
    
    private final OrderService orderService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Order>>> getAllOrders(
            @RequestHeader("X-Store-Id") Long storeId) {
        List<Order> orders = orderService.getAllOrders(storeId);
        return ResponseEntity.ok(ApiResponse.success(orders, "Orders retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> getOrder(
            @PathVariable Long id,
            @RequestHeader("X-Store-Id") Long storeId) {
        Order order = orderService.getOrder(id, storeId);
        return ResponseEntity.ok(ApiResponse.success(order));
    }
    
    @GetMapping("/buyer/{buyerId}")
    public ResponseEntity<ApiResponse<List<Order>>> getOrdersByBuyer(
            @PathVariable Long buyerId,
            @RequestHeader("X-Store-Id") Long storeId) {
        List<Order> orders = orderService.getOrdersByBuyer(buyerId, storeId);
        return ResponseEntity.ok(ApiResponse.success(orders));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Order>> createOrder(
            @RequestBody Order order,
            @RequestHeader("X-Store-Id") Long storeId) {
        Order created = orderService.createOrder(order, storeId);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success(created, "Order created successfully"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Order>> updateOrder(
            @PathVariable Long id,
            @RequestBody Order order,
            @RequestHeader("X-Store-Id") Long storeId) {
        Order updated = orderService.updateOrder(id, order, storeId);
        return ResponseEntity.ok(ApiResponse.success(updated, "Order updated"));
    }
}

