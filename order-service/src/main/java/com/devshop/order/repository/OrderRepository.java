package com.devshop.order.repository;

import com.devshop.order.model.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Long> {
    
    Optional<Order> findByIdAndStoreId(Long id, Long storeId);
    
    @Query("SELECT o FROM Order o WHERE o.storeId = :storeId AND o.buyerId = :buyerId")
    List<Order> findByBuyerAndStore(Long buyerId, Long storeId);
    
    @Query("SELECT o FROM Order o WHERE o.storeId = :storeId")
    List<Order> findAllByStore(Long storeId);
}

