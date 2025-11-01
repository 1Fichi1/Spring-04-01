package com.example.soratech.service;

import com.example.soratech.model.Category;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface CategoryService {
    Page<Category> findAllActive(Pageable pageable);
    
    Page<Category> findAllDeleted(Pageable pageable);
    
    Page<Category> searchByName(String name, Pageable pageable);
    
    List<Category> findAllActive();
    
    Category findById(Long id);
    
    void save(Category category);
    
    void update(Long id, Category category);
    
    void logicDelete(Long id);
    
    void logicDeleteAllByIds(List<Long> ids);
    
    void delete(Long id);
    
    void deleteAllByIds(List<Long> ids);
    
    void restore(Long id);
}
