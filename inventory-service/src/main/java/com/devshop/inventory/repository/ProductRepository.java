package com.devshop.inventory.repository;

import com.devshop.inventory.model.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Long> {
    
    Optional<Product> findByIdAndStoreId(Long id, Long storeId);
    
    Optional<Product> findBySkuAndStoreId(String sku, Long storeId);
    
    @Query("SELECT p FROM Product p WHERE p.storeId = :storeId AND p.isActive = true")
    List<Product> findAllActiveByStoreId(Long storeId);
}

