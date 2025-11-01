package com.example.soratech.controller;

import com.example.soratech.dto.RegisterRequest;
import com.example.soratech.model.Role;
import com.example.soratech.model.User;
import com.example.soratech.repository.RoleRepository;
import com.example.soratech.repository.UserRepository;
import jakarta.validation.Valid;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@Controller
public class AuthController {

    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    public AuthController(UserRepository userRepository, 
                         RoleRepository roleRepository,
                         PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.passwordEncoder = passwordEncoder;
    }

    // Страницы login и register удалены - теперь используются модальные окна

    @PostMapping("/register")
    public String register(@Valid @ModelAttribute RegisterRequest registerRequest,
                          BindingResult result,
                          Model model) {
        // Проверка на совпадение паролей
        if (!registerRequest.getPassword().equals(registerRequest.getConfirmPassword())) {
            result.rejectValue("confirmPassword", "error.registerRequest", "Пароли не совпадают");
        }

        // Проверка на существование email
        if (userRepository.existsByEmail(registerRequest.getEmail())) {
            result.rejectValue("email", "error.registerRequest", "Пользователь с таким email уже существует");
        }

        // Проверка на существование телефона
        if (registerRequest.getPhone() != null && !registerRequest.getPhone().isEmpty() 
            && userRepository.existsByPhone(registerRequest.getPhone())) {
            result.rejectValue("phone", "error.registerRequest", "Пользователь с таким телефоном уже существует");
        }

        if (result.hasErrors()) {
            String errorMsg = result.getAllErrors().get(0).getDefaultMessage();
            return "redirect:/?error=" + errorMsg;
        }

        try {
            // Создание нового пользователя
            User user = new User();
            user.setName(registerRequest.getName());
            user.setEmail(registerRequest.getEmail());
            user.setPhone(registerRequest.getPhone());
            user.setAddress(registerRequest.getAddress());
            user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
            user.setActive(true);
            user.setDeleted(false);
            user.setCreatedAt(LocalDateTime.now());

            // Назначение роли "Клиент" по умолчанию
            Role clientRole = roleRepository.findByName("Клиент")
                    .orElseThrow(() -> new RuntimeException("Роль 'Клиент' не найдена"));
            user.setRole(clientRole);

            userRepository.save(user);

            // Успешная регистрация - возвращаем успешный ответ
            model.addAttribute("success", "Регистрация успешна! Теперь вы можете войти.");
            return "redirect:/?registered=true";
        } catch (Exception e) {
            model.addAttribute("error", "Ошибка при регистрации: " + e.getMessage());
            return "redirect:/?error=" + e.getMessage();
        }
    }

    @PostMapping("/forgot-password")
    public String forgotPassword(@RequestParam String email, Model model) {
        // TODO: Реализовать логику восстановления пароля
        model.addAttribute("message", "Инструкции по восстановлению пароля отправлены на " + email);
        return "redirect:/?forgot=true";
    }

    @GetMapping("/api/auth/check")
    @ResponseBody
    public Map<String, Object> checkAuthentication() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        Map<String, Object> response = new HashMap<>();
        
        boolean isAuthenticated = authentication != null 
            && authentication.isAuthenticated() 
            && !"anonymousUser".equals(authentication.getPrincipal());
        
        response.put("authenticated", isAuthenticated);
        
        if (isAuthenticated) {
            response.put("username", authentication.getName());
            
            // Получаем роли пользователя
            java.util.List<String> roles = authentication.getAuthorities().stream()
                .map(auth -> auth.getAuthority())
                .collect(java.util.stream.Collectors.toList());
            response.put("roles", roles);
        }
        
        return response;
    }
}

