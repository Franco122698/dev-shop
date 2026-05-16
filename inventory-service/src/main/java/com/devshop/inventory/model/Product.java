package com.devshop.inventory.model;

import com.devshop.common.model.TenantEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

/**
 * Product entity with Optimistic Locking (version column)
 * This prevents race conditions during simultaneous stock deductions
 */
@Entity
@Table(name = "products", indexes = {
    @Index(name = "idx_store_id", columnList = "store_id"),
    @Index(name = "idx_sku", columnList = "sku")
})
@Data
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(callSuper = true)
public class Product extends TenantEntity {
    
    @Column(nullable = false)
    private String sku;
    
    @Column(nullable = false)
    private String name;
    
    @Column(columnDefinition = "TEXT")
    private String description;
    
    @Column(nullable = false)
    private BigDecimal price;
    
    @Column(nullable = false)
    private Integer stockCount;
    
    /**
     * Optimistic Locking version field:
     * - Incremented by Hibernate on each update
     * - If two threads try to update, the second will raise ObjectOptimisticLockingFailureException
     * - This prevents overselling in flash sales
     */
    @Version
    @Column(nullable = false)
    private Integer version;
    
    @Column(nullable = false)
    private Boolean isActive = true;
}

