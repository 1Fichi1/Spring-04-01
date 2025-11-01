package com.example.soratech.service;

import com.example.soratech.model.OrderItem;
import com.example.soratech.repository.OrderItemRepository;
import com.example.soratech.repository.OrderRepository;
import com.example.soratech.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class OrderItemServiceImpl implements OrderItemService {

    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public OrderItemServiceImpl(OrderItemRepository orderItemRepository,
                               OrderRepository orderRepository,
                               ProductRepository productRepository) {
        this.orderItemRepository = orderItemRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    @Override
    public List<OrderItem> findByOrderId(Long orderId) {
        return orderItemRepository.findByOrderId(orderId);
    }

    @Override
    public List<OrderItem> findByProductId(Long productId) {
        return orderItemRepository.findByProductId(productId);
    }

    @Override
    public OrderItem findById(Long id) {
        return orderItemRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Позиция заказа с ID " + id + " не найдена"));
    }

    @Override
    public void save(OrderItem orderItem, Long orderId, Long productId) {
        orderItem.setOrder(orderRepository.findById(orderId)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + orderId + " не найден")));
        orderItem.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт с ID " + productId + " не найден")));
        orderItemRepository.save(orderItem);
    }

    @Override
    public void update(Long id, OrderItem orderItem) {
        OrderItem existing = findById(id);
        existing.setQuantity(orderItem.getQuantity());
        existing.setPrice(orderItem.getPrice());
        orderItemRepository.save(existing);
    }

    @Override
    public void delete(Long id) {
        OrderItem orderItem = findById(id);
        orderItemRepository.delete(orderItem);
    }

    @Override
    public void deleteByOrderId(Long orderId) {
        orderItemRepository.deleteByOrderId(orderId);
    }
}




