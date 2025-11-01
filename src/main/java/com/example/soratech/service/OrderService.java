package com.example.soratech.service;

import com.example.soratech.model.Order;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.time.LocalDateTime;
import java.util.List;

public interface OrderService {
    Page<Order> findAllActive(Pageable pageable);
    
    Page<Order> findAllDeleted(Pageable pageable);
    
    Page<Order> findByUserId(Long userId, Pageable pageable);
    
    Page<Order> findByStatus(String status, Pageable pageable);
    
    Page<Order> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable);
    
    List<Order> findAllActive();
    
    Order findById(Long id);
    
    void save(Order order, Long userId);
    
    void update(Long id, Order order);
    
    void updateStatus(Long id, String status);
    
    void logicDelete(Long id);
    
    void logicDeleteAllByIds(List<Long> ids);
    
    void delete(Long id);
    
    void deleteAllByIds(List<Long> ids);
    
    void restore(Long id);
}



