package com.example.soratech.repository;

import com.example.soratech.model.Review;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReviewRepository extends JpaRepository<Review, Long> {
    Page<Review> findAllByDeletedFalse(Pageable pageable);
    
    Page<Review> findAllByDeletedTrue(Pageable pageable);
    
    Page<Review> findByProductIdAndDeletedFalse(Long productId, Pageable pageable);
    
    Page<Review> findByUserIdAndDeletedFalse(Long userId, Pageable pageable);
    
    Page<Review> findByRatingAndDeletedFalse(Integer rating, Pageable pageable);
}



