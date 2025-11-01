package com.example.soratech.service;

import com.example.soratech.model.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface UserService {
    Page<User> findAllActive(Pageable pageable);
    
    Page<User> findAllDeleted(Pageable pageable);
    
    Page<User> searchByName(String name, Pageable pageable);
    
    Page<User> searchByEmail(String email, Pageable pageable);
    
    List<User> findAllActive();
    
    User findById(Long id);
    
    User findByEmail(String email);
    
    User findByPhone(String phone);
    
    void save(User user, Long roleId);
    
    void update(Long id, User user, Long roleId);
    
    void logicDelete(Long id);
    
    void logicDeleteAllByIds(List<Long> ids);
    
    void delete(Long id);
    
    void deleteAllByIds(List<Long> ids);
    
    void restore(Long id);
    
    boolean existsByEmail(String email);
    
    boolean existsByPhone(String phone);
}



