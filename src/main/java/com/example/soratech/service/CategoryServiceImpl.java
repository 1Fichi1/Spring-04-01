package com.example.soratech.service;

import com.example.soratech.model.Category;
import com.example.soratech.repository.CategoryRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryServiceImpl(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    @Override
    public Page<Category> findAllActive(Pageable pageable) {
        return categoryRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Category> findAllDeleted(Pageable pageable) {
        return categoryRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Category> searchByName(String name, Pageable pageable) {
        return categoryRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    @Override
    public List<Category> findAllActive() {
        return categoryRepository.findAllByDeletedFalse();
    }

    @Override
    public Category findById(Long id) {
        return categoryRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Категория с ID " + id + " не найдена"));
    }

    @Override
    public void save(Category category) {
        category.setDeleted(false);
        categoryRepository.save(category);
    }

    @Override
    public void update(Long id, Category category) {
        Category existing = findById(id);
        existing.setName(category.getName());
        existing.setDescription(category.getDescription());
        categoryRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Category category = findById(id);
        category.setDeleted(true);
        categoryRepository.save(category);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Category> categories = categoryRepository.findAllById(ids);
        categories.forEach(category -> category.setDeleted(true));
        categoryRepository.saveAll(categories);
    }

    @Override
    public void delete(Long id) {
        Category category = findById(id);
        categoryRepository.delete(category);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        categoryRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Category category = findById(id);
        category.setDeleted(false);
        categoryRepository.save(category);
    }
}
