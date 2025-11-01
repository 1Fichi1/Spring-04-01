package com.example.soratech.service;

import com.example.soratech.model.Order;
import com.example.soratech.repository.OrderRepository;
import com.example.soratech.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class OrderServiceImpl implements OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;

    public OrderServiceImpl(OrderRepository orderRepository, UserRepository userRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Order> findAllActive(Pageable pageable) {
        return orderRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Order> findAllDeleted(Pageable pageable) {
        return orderRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Order> findByUserId(Long userId, Pageable pageable) {
        return orderRepository.findByUserIdAndDeletedFalse(userId, pageable);
    }

    @Override
    public Page<Order> findByStatus(String status, Pageable pageable) {
        return orderRepository.findByStatusAndDeletedFalse(status, pageable);
    }

    @Override
    public Page<Order> findByDateRange(LocalDateTime start, LocalDateTime end, Pageable pageable) {
        return orderRepository.findByCreatedAtBetweenAndDeletedFalse(start, end, pageable);
    }

    @Override
    public List<Order> findAllActive() {
        return orderRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public Order findById(Long id) {
        return orderRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Заказ с ID " + id + " не найден"));
    }

    @Override
    public void save(Order order, Long userId) {
        order.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден")));
        order.setDeleted(false);
        order.setCreatedAt(LocalDateTime.now());
        orderRepository.save(order);
    }

    @Override
    public void update(Long id, Order order) {
        Order existing = findById(id);
        existing.setTotalAmount(order.getTotalAmount());
        existing.setStatus(order.getStatus());
        existing.setDeliveryAddress(order.getDeliveryAddress());
        existing.setComment(order.getComment());
        existing.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(existing);
    }

    @Override
    public void updateStatus(Long id, String status) {
        Order existing = findById(id);
        existing.setStatus(status);
        existing.setUpdatedAt(LocalDateTime.now());
        orderRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Order order = findById(id);
        order.setDeleted(true);
        orderRepository.save(order);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Order> orders = orderRepository.findAllById(ids);
        orders.forEach(order -> order.setDeleted(true));
        orderRepository.saveAll(orders);
    }

    @Override
    public void delete(Long id) {
        Order order = findById(id);
        orderRepository.delete(order);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        orderRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Order order = findById(id);
        order.setDeleted(false);
        orderRepository.save(order);
    }
}




