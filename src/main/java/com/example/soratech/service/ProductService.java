package com.example.soratech.service;

import com.example.soratech.model.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    Page<Product> findAllActive(Pageable pageable);

    Page<Product> findAllDeleted(Pageable pageable);

    Page<Product> searchByName(String query, Pageable pageable);

    Page<Product> filterByCategory(Long categoryId, Pageable pageable);

    Page<Product> filterByManufacturer(Long manufacturerId, Pageable pageable);

    Page<Product> filterBySupplier(Long supplierId, Pageable pageable);

    List<Product> findAllActive();

    Product findById(Long id);

    void save(Product product, Long manufacturerId, Long categoryId, List<Long> supplierIds);

    void update(Long id, Product product, Long manufacturerId, Long categoryId, List<Long> supplierIds);

    void logicDelete(Long id);

    void logicDeleteAllByIds(List<Long> ids);

    void delete(Long id);

    void deleteAllByIds(List<Long> ids);

    void restore(Long id);
    
    Page<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable);
}



