package com.example.soratech.service;

import com.example.soratech.model.ProductDetails;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface ProductDetailsService {
    Page<ProductDetails> findAllActive(Pageable pageable);
    
    Page<ProductDetails> findAllDeleted(Pageable pageable);
    
    Page<ProductDetails> searchByCharacteristicName(String characteristicName, Pageable pageable);
    
    List<ProductDetails> findByProductId(Long productId);
    
    Page<ProductDetails> findByProductId(Long productId, Pageable pageable);
    
    List<ProductDetails> findAllActive();
    
    ProductDetails findById(Long id);
    
    void save(ProductDetails productDetails);
    
    void update(Long id, ProductDetails productDetails);
    
    void logicDelete(Long id);
    
    void delete(Long id);
    
    void restore(Long id);
}



