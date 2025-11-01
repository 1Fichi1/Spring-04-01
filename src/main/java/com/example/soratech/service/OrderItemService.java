package com.example.soratech.service;

import com.example.soratech.model.OrderItem;

import java.util.List;

public interface OrderItemService {
    List<OrderItem> findByOrderId(Long orderId);
    
    List<OrderItem> findByProductId(Long productId);
    
    OrderItem findById(Long id);
    
    void save(OrderItem orderItem, Long orderId, Long productId);
    
    void update(Long id, OrderItem orderItem);
    
    void delete(Long id);
    
    void deleteByOrderId(Long orderId);
}



