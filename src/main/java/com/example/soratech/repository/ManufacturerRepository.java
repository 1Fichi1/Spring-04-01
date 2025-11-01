package com.example.soratech.repository;

import com.example.soratech.model.Manufacturer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ManufacturerRepository extends JpaRepository<Manufacturer, Long> {
    Page<Manufacturer> findAllByDeletedFalse(Pageable pageable);
    
    Page<Manufacturer> findAllByDeletedTrue(Pageable pageable);
    
    Page<Manufacturer> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
    
    Page<Manufacturer> findByCountryAndDeletedFalse(String country, Pageable pageable);
    
    List<Manufacturer> findAllByDeletedFalse();
}



