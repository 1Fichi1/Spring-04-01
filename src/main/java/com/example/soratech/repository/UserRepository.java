package com.example.soratech.repository;

import com.example.soratech.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Page<User> findAllByDeletedFalse(Pageable pageable);
    
    Page<User> findAllByDeletedTrue(Pageable pageable);
    
    Optional<User> findByEmail(String email);
    
    Optional<User> findByPhone(String phone);
    
    Optional<User> findByEmailAndDeletedFalse(String email);
    
    Optional<User> findByPhoneAndDeletedFalse(String phone);
    
    Page<User> findByNameContainingIgnoreCaseAndDeletedFalse(String name, Pageable pageable);
    
    Page<User> findByEmailContainingIgnoreCaseAndDeletedFalse(String email, Pageable pageable);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
}


