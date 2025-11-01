package com.example.soratech.service;

import com.example.soratech.model.Supplier;
import com.example.soratech.model.Product;
import com.example.soratech.repository.ManufacturerRepository;
import com.example.soratech.repository.CategoryRepository;
import com.example.soratech.repository.SupplierRepository;
import com.example.soratech.repository.ProductRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@Transactional
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final ManufacturerRepository manufacturerRepository;
    private final CategoryRepository categoryRepository;
    private final SupplierRepository supplierRepository;

    public ProductServiceImpl(ProductRepository productRepository,
                             ManufacturerRepository manufacturerRepository,
                             CategoryRepository categoryRepository,
                             SupplierRepository supplierRepository) {
        this.productRepository = productRepository;
        this.manufacturerRepository = manufacturerRepository;
        this.categoryRepository = categoryRepository;
        this.supplierRepository = supplierRepository;
    }

    @Override
    public Page<Product> findAllActive(Pageable pageable) {
        return productRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Product> findAllDeleted(Pageable pageable) {
        return productRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Product> searchByName(String query, Pageable pageable) {
        return productRepository.findByNameContainingIgnoreCaseAndDeletedFalse(query, pageable);
    }

    @Override
    public Page<Product> filterByCategory(Long categoryId, Pageable pageable) {
        return productRepository.findByCategoryIdAndDeletedFalse(categoryId, pageable);
    }

    @Override
    public Page<Product> filterByManufacturer(Long manufacturerId, Pageable pageable) {
        return productRepository.findByManufacturerIdAndDeletedFalse(manufacturerId, pageable);
    }

    @Override
    public Page<Product> filterBySupplier(Long supplierId, Pageable pageable) {
        return productRepository.findBySuppliersIdAndDeletedFalse(supplierId, pageable);
    }

    @Override
    public List<Product> findAllActive() {
        return productRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public Product findById(Long id) {
        return productRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Продукт с ID " + id + " не найден"));
    }

    @Override
    public void save(Product product, Long manufacturerId, Long categoryId, List<Long> supplierIds) {
        product.setManufacturer(manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Производитель с ID " + manufacturerId + " не найден")));
        product.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + categoryId + " не найдена")));
        if (supplierIds != null) {
            Set<Supplier> suppliers = supplierRepository.findAllById(supplierIds).stream()
                    .collect(Collectors.toSet());
            product.setSuppliers(suppliers);
        }
        product.setDeleted(false);
        productRepository.save(product);
    }

    @Override
    public void update(Long id, Product product, Long manufacturerId, Long categoryId, List<Long> supplierIds) {
        Product existing = findById(id);
        existing.setName(product.getName());
        existing.setPrice(product.getPrice());
        existing.setStockQuantity(product.getStockQuantity());
        existing.setManufacturer(manufacturerRepository.findById(manufacturerId)
                .orElseThrow(() -> new EntityNotFoundException("Производитель с ID " + manufacturerId + " не найден")));
        existing.setCategory(categoryRepository.findById(categoryId)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + categoryId + " не найдена")));
        if (supplierIds != null) {
            Set<Supplier> suppliers = supplierRepository.findAllById(supplierIds).stream()
                    .collect(Collectors.toSet());
            existing.setSuppliers(suppliers);
        } else {
            existing.setSuppliers(Set.of());
        }
        productRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Product product = findById(id);
        product.setDeleted(true);
        productRepository.save(product);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Product> products = productRepository.findAllById(ids);
        products.forEach(product -> product.setDeleted(true));
        productRepository.saveAll(products);
    }

    @Override
    public void delete(Long id) {
        Product product = findById(id);
        productRepository.delete(product);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        productRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Product product = findById(id);
        product.setDeleted(false);
        productRepository.save(product);
    }
    
    @Override
    public Page<Product> filterByPriceRange(BigDecimal minPrice, BigDecimal maxPrice, Pageable pageable) {
        if (minPrice != null && maxPrice != null) {
            return productRepository.findByPriceBetweenAndDeletedFalse(minPrice, maxPrice, pageable);
        } else if (minPrice != null) {
            return productRepository.findByPriceGreaterThanEqualAndDeletedFalse(minPrice, pageable);
        } else if (maxPrice != null) {
            return productRepository.findByPriceLessThanEqualAndDeletedFalse(maxPrice, pageable);
        } else {
            return findAllActive(pageable);
        }
    }
}



