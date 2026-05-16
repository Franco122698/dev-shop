package com.devshop.order.model;

import com.devshop.common.model.TenantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Order entity with multi-tenancy support
 * Each order belongs to a specific store (white-label customer)
 */
@Entity
@Table(name = "orders", indexes = {
    @Index(name = "idx_store_id", columnList = "store_id"),
    @Index(name = "idx_buyer_id", columnList = "buyer_id"),
    @Index(name = "idx_status", columnList = "status")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Order extends TenantEntity {
    
    @Column(nullable = false)
    private Long buyerId;
    
    @Column(nullable = false)
    private Long productId;
    
    @Column(nullable = false)
    private Integer quantity;
    
    @Column(nullable = false)
    private BigDecimal totalPrice;
    
    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private OrderStatus status = OrderStatus.PENDING;
    
    @Column(columnDefinition = "TEXT")
    private String failureReason;
    
    public enum OrderStatus {
        PENDING,
        PAID,
        FAILED,
        CANCELLED
    }
}

