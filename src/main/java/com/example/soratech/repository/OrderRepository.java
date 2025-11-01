package com.example.soratech.repository;

import com.example.soratech.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;

public interface OrderRepository extends JpaRepository<Order, Long> {
    Page<Order> findAllByDeletedFalse(Pageable pageable);
    
    Page<Order> findAllByDeletedTrue(Pageable pageable);
    
    Page<Order> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
    
    Page<Order> findByStatusAndDeletedFalse(String status, Pageable pageable);
    
    Page<Order> findByCreatedAtBetweenAndDeletedFalse(LocalDateTime start, LocalDateTime end, Pageable pageable);
}



