package com.example.soratech.repository;

import com.example.soratech.model.Supplier;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface SupplierRepository extends JpaRepository<Supplier, Long> {
    Page<Supplier> findAllByDeletedFalse(Pageable pageable);
    
    Page<Supplier> findAllByDeletedTrue(Pageable pageable);
    
    Page<Supplier> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
    
    List<Supplier> findAllByDeletedFalse();
}



