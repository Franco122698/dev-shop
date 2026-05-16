package com.devshop.inventory.service;

import com.devshop.inventory.model.Product;
import com.devshop.inventory.repository.ProductRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.OptimisticLockingFailureException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class ProductService {
    
    private final ProductRepository productRepository;
    
    @Transactional(readOnly = true)
    public List<Product> getAllProducts(Long storeId) {
        return productRepository.findAllActiveByStoreId(storeId);
    }
    
    @Transactional(readOnly = true)
    public Product getProduct(Long id, Long storeId) {
        return productRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
    }
    
    @Transactional
    public Product createProduct(Product product) {
        return productRepository.save(product);
    }
    
    @Transactional
    public Product updateProduct(Long id, Product productDetails, Long storeId) {
        Product product = getProduct(id, storeId);
        product.setName(productDetails.getName());
        product.setDescription(productDetails.getDescription());
        product.setPrice(productDetails.getPrice());
        product.setStockCount(productDetails.getStockCount());
        return productRepository.save(product);
    }
    
    /**
     * Deduct stock with optimistic locking.
     * If two requests try to deduct simultaneously, the second will fail with OptimisticLockingFailureException
     */
    @Transactional
    public Product deductStock(Long id, Integer quantity, Long storeId) {
        Product product = productRepository.findByIdAndStoreId(id, storeId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));
        
        if (product.getStockCount() < quantity) {
            throw new IllegalArgumentException("Insufficient stock available");
        }
        
        try {
            product.setStockCount(product.getStockCount() - quantity);
            return productRepository.save(product);
        } catch (OptimisticLockingFailureException e) {
            log.error("Optimistic locking failed for product {}: {}", id, e.getMessage());
            throw new RuntimeException("Stock was updated by another request. Please retry.");
        }
    }
    
    @Transactional
    public void deleteProduct(Long id, Long storeId) {
        Product product = getProduct(id, storeId);
        product.setIsActive(false);
        productRepository.save(product);
    }
}

