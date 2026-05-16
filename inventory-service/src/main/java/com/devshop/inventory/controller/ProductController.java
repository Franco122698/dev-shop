package com.devshop.inventory.controller;

import com.devshop.common.dto.ApiResponse;
import com.devshop.inventory.model.Product;
import com.devshop.inventory.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/inventory/products")
@RequiredArgsConstructor
public class ProductController {
    
    private final ProductService productService;
    
    @GetMapping
    public ResponseEntity<ApiResponse<List<Product>>> getAllProducts(
            @RequestHeader("X-Store-Id") Long storeId) {
        List<Product> products = productService.getAllProducts(storeId);
        return ResponseEntity.ok(ApiResponse.success(products, "Products retrieved successfully"));
    }
    
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> getProduct(
            @PathVariable Long id,
            @RequestHeader("X-Store-Id") Long storeId) {
        Product product = productService.getProduct(id, storeId);
        return ResponseEntity.ok(ApiResponse.success(product));
    }
    
    @PostMapping
    public ResponseEntity<ApiResponse<Product>> createProduct(
            @RequestBody Product product,
            @RequestHeader("X-Store-Id") Long storeId) {
        product.setStoreId(storeId);
        Product created = productService.createProduct(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success(created, "Product created"));
    }
    
    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Product>> updateProduct(
            @PathVariable Long id,
            @RequestBody Product product,
            @RequestHeader("X-Store-Id") Long storeId) {
        Product updated = productService.updateProduct(id, product, storeId);
        return ResponseEntity.ok(ApiResponse.success(updated, "Product updated"));
    }
    
    @PostMapping("/{id}/deduct")
    public ResponseEntity<ApiResponse<Product>> deductStock(
            @PathVariable Long id,
            @RequestParam Integer quantity,
            @RequestHeader("X-Store-Id") Long storeId) {
        Product product = productService.deductStock(id, quantity, storeId);
        return ResponseEntity.ok(ApiResponse.success(product, "Stock deducted successfully"));
    }
}

