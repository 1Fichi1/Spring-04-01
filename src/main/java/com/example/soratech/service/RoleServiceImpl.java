package com.example.soratech.service;

import com.example.soratech.model.Role;
import com.example.soratech.repository.RoleRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional
public class RoleServiceImpl implements RoleService {

    private final RoleRepository roleRepository;

    public RoleServiceImpl(RoleRepository roleRepository) {
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<Role> findAllActive(Pageable pageable) {
        return roleRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<Role> findAllDeleted(Pageable pageable) {
        return roleRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<Role> searchByName(String name, Pageable pageable) {
        return roleRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    @Override
    public List<Role> findAllActive() {
        return roleRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public Role findById(Long id) {
        return roleRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Роль с ID " + id + " не найдена"));
    }

    @Override
    public Role findByName(String name) {
        return roleRepository.findByNameAndDeletedFalse(name)
                .orElseThrow(() -> new EntityNotFoundException("Роль " + name + " не найдена"));
    }

    @Override
    public void save(Role role) {
        role.setDeleted(false);
        roleRepository.save(role);
    }

    @Override
    public void update(Long id, Role role) {
        Role existing = findById(id);
        existing.setName(role.getName());
        existing.setDescription(role.getDescription());
        roleRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        Role role = findById(id);
        role.setDeleted(true);
        roleRepository.save(role);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<Role> roles = roleRepository.findAllById(ids);
        roles.forEach(role -> role.setDeleted(true));
        roleRepository.saveAll(roles);
    }

    @Override
    public void delete(Long id) {
        Role role = findById(id);
        roleRepository.delete(role);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        roleRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        Role role = findById(id);
        role.setDeleted(false);
        roleRepository.save(role);
    }
}



