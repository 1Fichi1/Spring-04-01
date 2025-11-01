package com.example.soratech.service;

import com.example.soratech.model.Role;
import com.example.soratech.model.User;
import com.example.soratech.repository.RoleRepository;
import com.example.soratech.repository.UserRepository;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;

    public UserServiceImpl(UserRepository userRepository, RoleRepository roleRepository) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
    }

    @Override
    public Page<User> findAllActive(Pageable pageable) {
        return userRepository.findAllByDeletedFalse(pageable);
    }

    @Override
    public Page<User> findAllDeleted(Pageable pageable) {
        return userRepository.findAllByDeletedTrue(pageable);
    }

    @Override
    public Page<User> searchByName(String name, Pageable pageable) {
        return userRepository.findByNameContainingIgnoreCaseAndDeletedFalse(name, pageable);
    }

    @Override
    public Page<User> searchByEmail(String email, Pageable pageable) {
        return userRepository.findByEmailContainingIgnoreCaseAndDeletedFalse(email, pageable);
    }

    @Override
    public List<User> findAllActive() {
        return userRepository.findAllByDeletedFalse(Pageable.unpaged()).getContent();
    }

    @Override
    public User findById(Long id) {
        return userRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с ID " + id + " не найден"));
    }

    @Override
    public User findByEmail(String email) {
        return userRepository.findByEmailAndDeletedFalse(email)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с email " + email + " не найден"));
    }

    @Override
    public User findByPhone(String phone) {
        return userRepository.findByPhoneAndDeletedFalse(phone)
                .orElseThrow(() -> new EntityNotFoundException("Пользователь с телефоном " + phone + " не найден"));
    }

    @Override
    public void save(User user, Long roleId) {
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Роль с ID " + roleId + " не найдена"));
            user.setRole(role);
        }
        user.setDeleted(false);
        user.setCreatedAt(LocalDateTime.now());
        userRepository.save(user);
    }

    @Override
    public void update(Long id, User user, Long roleId) {
        User existing = findById(id);
        existing.setEmail(user.getEmail());
        existing.setName(user.getName());
        existing.setPhone(user.getPhone());
        existing.setAddress(user.getAddress());
        existing.setActive(user.isActive());
        if (user.getPassword() != null && !user.getPassword().isEmpty()) {
            existing.setPassword(user.getPassword());
        }
        if (roleId != null) {
            Role role = roleRepository.findById(roleId)
                    .orElseThrow(() -> new EntityNotFoundException("Роль с ID " + roleId + " не найдена"));
            existing.setRole(role);
        }
        userRepository.save(existing);
    }

    @Override
    public void logicDelete(Long id) {
        User user = findById(id);
        user.setDeleted(true);
        userRepository.save(user);
    }

    @Override
    public void logicDeleteAllByIds(List<Long> ids) {
        List<User> users = userRepository.findAllById(ids);
        users.forEach(user -> user.setDeleted(true));
        userRepository.saveAll(users);
    }

    @Override
    public void delete(Long id) {
        User user = findById(id);
        userRepository.delete(user);
    }

    @Override
    public void deleteAllByIds(List<Long> ids) {
        userRepository.deleteAllById(ids);
    }

    @Override
    public void restore(Long id) {
        User user = findById(id);
        user.setDeleted(false);
        userRepository.save(user);
    }

    @Override
    public boolean existsByEmail(String email) {
        return userRepository.existsByEmail(email);
    }

    @Override
    public boolean existsByPhone(String phone) {
        return userRepository.existsByPhone(phone);
    }
}



