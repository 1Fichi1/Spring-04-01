package com.example.soratech.service;

import com.example.soratech.model.Review;
import com.example.soratech.repository.ReviewRepository;
import com.example.soratech.repository.ProductRepository;
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
public class ReviewServiceImpl implements ReviewService {

    private final ReviewRepository reviewRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    public ReviewServiceImpl(ReviewRepository reviewRepository,
                            ProductRepository productRepository,
                            UserRepository userRepository) {
        this.reviewRepository = reviewRepository;
        this.productRepository = productRepository;
        this.userRepository = userRepository;
    }

    @Override
    public Page<Review> findAllActive(Pageable pageable) {
        return reviewRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Review> findAllDeleted(Pageable pageable) {
        return reviewRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Review> findByProductId(Long productId, Pageable pageable) {
        return reviewRepository.findByProductIdAndDeletedFalse(productId, pageable);
    }

    @Override
    public Page<Review> findByUserId(Long userId, Pageable pageable) {
        return reviewRepository.findByUserIdAndDeletedFalse(userId, pageable);
    }

    @Override
    public Page<Review> findByRating(Integer rating, Pageable pageable) {
        return reviewRepository.findByRatingAndDeletedFalse(rating, pageable);
    }

    @Override
    public List<Review> findAllActive() {
        return reviewRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public Review findById(Long id) {
        return reviewRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Отзыв с ID " + id + " не найден"));
    }

    @Override
    public void save(Review review, Long productId, Long userId) {
        review.setProduct(productRepository.findById(productId)
                .orElseThrow(() -> new EntityNotFoundException("Продукт с ID " + productId + " не найден")));
        review.setUser(userRepository.findById(userId)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + userId + " не найден")));
        review.setDeleted(false);
        review.setCreatedAt(LocalDateTime.now());
        reviewRepository.save(review);
    }

    @Override
    public void update(Long id, Review review) {
        Review existing = findById(id);
        existing.setRating(review.getRating());
        existing.setComment(review.getComment());
        existing.setUpdatedAt(LocalDateTime.now());
        reviewRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Review review = findById(id);
        review.setDeleted(true);
        reviewRepository.save(review);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Review> reviews = reviewRepository.findAllById(ids);
        reviews.forEach(review -> review.setDeleted(true));
        reviewRepository.saveAll(reviews);
    }

    @Override
    public void delete(Long id) {
        Review review = findById(id);
        reviewRepository.delete(review);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        reviewRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Review review = findById(id);
        review.setDeleted(false);
        reviewRepository.save(review);
    }
}




