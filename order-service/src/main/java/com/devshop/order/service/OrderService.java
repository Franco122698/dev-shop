package com.devshop.order.service;

import com.devshop.order.client.InventoryServiceClient;
import com.devshop.order.model.Order;
import com.devshop.order.repository.OrderRepository;
import io.github.resilience4j.circuitbreaker.annotation.CircuitBreaker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class OrderService {
    
    private final OrderRepository orderRepository;
    private final InventoryServiceClient inventoryServiceClient;
    
    @Transactional(readOnly = true)
    public List<Order> getAllOrders(Long storeId) {
        return orderRepository.findAllByStore(storeId);
    }
    
    @Transactional(readOnly = true)
    public Order getOrder(Long id, Long storeId) {
        return orderRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Order not found"));
    }
    
    @Transactional(readOnly = true)
    public List<Order> getOrdersByBuyer(Long buyerId, Long storeId) {
        return orderRepository.findByBuyerAndStore(buyerId, storeId);
    }
    
    /**
     * Create order with inter-service communication to inventory service
     * Uses circuit breaker to handle inventory service failures gracefully
     */
    @Transactional
    @CircuitBreaker(name = "inventoryService", fallbackMethod = "createOrderFallback")
    public Order createOrder(Order order, Long storeId) {
        order.setStoreId(storeId);
        order.setStatus(Order.OrderStatus.PENDING);
        
        try {
            // Call inventory service to deduct stock
            var response = inventoryServiceClient.deductStock(
                    order.getProductId(),
                    order.getQuantity(),
                    storeId
            );
            
            if (!response.isSuccess()) {
                order.setStatus(Order.OrderStatus.FAILED);
                order.setFailureReason(response.getMessage());
                log.warn("Stock deduction failed: {}", response.getMessage());
                return orderRepository.save(order);
            }
            
            order.setStatus(Order.OrderStatus.PAID);
            return orderRepository.save(order);
            
        } catch (Exception e) {
            log.error("Error creating order: {}", e.getMessage());
            order.setStatus(Order.OrderStatus.FAILED);
            order.setFailureReason(e.getMessage());
            return orderRepository.save(order);
        }
    }
    
    /**
     * Fallback method when inventory service is unavailable
     */
    public Order createOrderFallback(Order order, Long storeId, Exception e) {
        log.error("Inventory service unavailable, creating order with FAILED status", e);
        order.setStoreId(storeId);
        order.setStatus(Order.OrderStatus.FAILED);
        order.setFailureReason("Inventory service temporarily unavailable");
        return orderRepository.save(order);
    }
    
    @Transactional
    public Order updateOrder(Long id, Order orderDetails, Long storeId) {
        Order order = getOrder(id, storeId);
        order.setStatus(orderDetails.getStatus());
        return orderRepository.save(order);
    }
}

