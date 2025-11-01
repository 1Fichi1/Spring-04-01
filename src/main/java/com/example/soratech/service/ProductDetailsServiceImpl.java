package com.example.soratech.service;

import com.example.soratech.model.ProductDetails;
import com.example.soratech.repository.ProductDetailsRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class ProductDetailsServiceImpl implements ProductDetailsService {

    private final ProductDetailsRepository productDetailsRepository;

    public ProductDetailsServiceImpl(ProductDetailsRepository productDetailsRepository) {
        this.productDetailsRepository = productDetailsRepository;
    }

    @Override
    public Page<ProductDetails> findAllActive(Pageable pageable) {
        return productDetailsRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<ProductDetails> findAllDeleted(Pageable pageable) {
        return productDetailsRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<ProductDetails> searchByCharacteristicName(String characteristicName, Pageable pageable) {
        return productDetailsRepository.findByCharacteristicNameContainingIgnoreCaseAndDeletedFalse(characteristicName, pageable);
    }

    @Override
    public List<ProductDetails> findByProductId(Long productId) {
        return productDetailsRepository.findByProductIdAndDeletedFalse(productId);
    }

    @Override
    public Page<ProductDetails> findByProductId(Long productId, Pageable pageable) {
        return productDetailsRepository.findByProductIdAndDeletedFalse(productId, pageable);
    }

    @Override
    public List<ProductDetails> findAllActive() {
        return productDetailsRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public ProductDetails findById(Long id) {
        return productDetailsRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Детали продукта с ID " + id + " не найдены"));
    }

    @Override
    public void save(ProductDetails productDetails) {
        productDetails.setDeleted(false);
        productDetailsRepository.save(productDetails);
    }

    @Override
    public void update(Long id, ProductDetails productDetails) {
        ProductDetails existing = findById(id);
        existing.setCharacteristicName(productDetails.getCharacteristicName());
        existing.setCharacteristicValue(productDetails.getCharacteristicValue());
        if (productDetails.getProduct() != null) {
            existing.setProduct(productDetails.getProduct());
        }
        productDetailsRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        ProductDetails productDetails = findById(id);
        productDetails.setDeleted(true);
        productDetailsRepository.save(productDetails);
    }

    @Override
    public void delete(Long id) {
        ProductDetails productDetails = findById(id);
        productDetailsRepository.delete(productDetails);
    }

    @Override
    public void restore(Long id) {
        ProductDetails productDetails = findById(id);
        productDetails.setDeleted(false);
        productDetailsRepository.save(productDetails);
    }
}



