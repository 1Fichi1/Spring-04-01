package com.example.soratech.repository;

import com.example.soratech.model.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ProductDetailsRepository extends JpaRepository<ProductDetails, Long> {
    Page<ProductDetails> findAllByDeletedFalse(Pageable pageable);
    
    Page<ProductDetails> findAllByDeletedTrue(Pageable pageable);
    
    Page<ProductDetails> findByCharacteristicNameContainingIgnoreCaseAndDeletedFalse(String characteristicName, Pageable pageable);
    
    List<ProductDetails> findByProductIdAndDeletedFalse(Long productId);
    
    Page<ProductDetails> findByProductIdAndDeletedFalse(Long productId, Pageable pageable);
}



