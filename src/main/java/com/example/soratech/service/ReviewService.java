package com.example.soratech.service;

import com.example.soratech.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ReviewService {
    Page<Review> findAllActive(Pageable pageable);
    
    Page<Review> findAllDeleted(Pageable pageable);
    
    Page<Review> findByProductId(Long productId, Pageable pageable);
    
    Page<Review> findByUserId(Long userId, Pageable pageable);
    
    Page<Review> findByRating(Integer rating, Pageable pageable);
    
    List<Review> findAllActive();
    
    Review findById(Long id);
    
    void save(Review review, Long productId, Long userId);
    
    void update(Long id, Review review);
    
    void logicDelete(Long id);
    
    void logicDeleteAllByIds(List<Long> ids);
    
    void delete(Long id);
    
    void deleteAllByIds(List<Long> ids);
    
    void restore(Long id);
}



