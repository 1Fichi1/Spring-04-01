package com.example.soratech.repository;

import com.example.soratech.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface RoleRepository extends JpaRepository<Role, Long> {
    Page<Role> findAllByDeletedFalse(Pageable pageable);
    
    Page<Role> findAllByDeletedTrue(Pageable pageable);
    
    Optional<Role> findByName(String name);
    
    Optional<Role> findByNameAndDeletedFalse(String name);
    
    Page<Role> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
}


