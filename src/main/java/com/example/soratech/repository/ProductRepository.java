package com.example.soratech.repository;

import com.example.soratech.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigDecimal;

public interface ProductRepository extends JpaRepository<Product, Long> {
    Page<Product> findAllByDeletedFalse(Pageable pageable);

    Page<Product> findAllByDeletedTrue(Pageable pageable);

    Page<Product> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);

    Page<Product> findByCategoryIdAndDeletedFalse(Long categoryId, Pageable pageable);

    Page<Product> findByManufacturerIdAndDeletedFalse(Long manufacturerId, Pageable pageable);

    Page<Product> findBySuppliersIdAndDeletedFalse(Long supplierId, Pageable pageable);
    
    Page<Product> findByPriceBetweenAndDeletedFalse(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
    
    Page<Product> findByPriceGreaterThanEqualAndDeletedFalse(BigDecimal minPrice, Pageable pageable);
    
    Page<Product> findByPriceLessThanEqualAndDeletedFalse(BigDecimal maxPrice, Pageable pageable);
}



