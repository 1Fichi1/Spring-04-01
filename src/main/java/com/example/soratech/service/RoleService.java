package com.example.soratech.service;

import com.example.soratech.model.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface RoleService {
    Page<Role> findAllActive(Pageable pageable);
    
    Page<Role> findAllDeleted(Pageable pageable);
    
    Page<Role> searchByName(String name, Pageable pageable);
    
    List<Role> findAllActive();
    
    Role findById(Long id);
    
    Role findByName(String name);
    
    void save(Role role);
    
    void update(Long id, Role role);
    
    void logicDelete(Long id);
    
    void logicDeleteAllByIds(List<Long> ids);
    
    void delete(Long id);
    
    void deleteAllByIds(List<Long> ids);
    
    void restore(Long id);
}



