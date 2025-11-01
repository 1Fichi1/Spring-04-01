package com.example.soratech.controller.api;

import com.example.soratech.dto.JwtAuthenticationResponse;
import com.example.soratech.dto.LoginRequest;
import com.example.soratech.dto.RegisterRequest;
import com.example.soratech.model.User;
import com.example.soratech.model.Role;
import com.example.soratech.repository.RoleRepository;
import com.example.soratech.repository.UserRepository;
import com.example.soratech.security.JwtTokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@Tag(name = "Authentication", description = "API для аутентификации и регистрации пользователей")
@RestController
@RequestMapping("/api/v1/auth")
public class RestAuthController {

    @Autowired
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtTokenProvider tokenProvider;

    @Operation(summary = "Вход в систему", description = "Аутентификация пользователя и получение JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешная аутентификация",
                    content = @Content(schema = @Schema(implementation = JwtAuthenticationResponse.class))),
            @ApiResponse(responseCode = "401", description = "Неверные учетные данные")
    })
    @PostMapping("/login")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginRequest.getUsername(),
                        loginRequest.getPassword()
                )
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = tokenProvider.generateToken(authentication);

        User user = userRepository.findByEmail(loginRequest.getUsername())
                .orElseGet(() -> userRepository.findByPhone(loginRequest.getUsername())
                        .orElse(null));

        String role = user != null && user.getRole() != null ? user.getRole().getName() : "Клиент";

        return ResponseEntity.ok(new JwtAuthenticationResponse(jwt, loginRequest.getUsername(), role));
    }

    @Operation(summary = "Регистрация", description = "Регистрация нового пользователя")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "201", description = "Пользователь успешно зарегистрирован"),
            @ApiResponse(responseCode = "400", description = "Некорректные данные или пользователь уже существует")
    })
    @PostMapping("/register")
    public ResponseEntity<?> registerUser(@Valid @RequestBody RegisterRequest registerRequest) {
        // Проверка существования пользователя
        if (userRepository.findByEmail(registerRequest.getEmail()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Пользователь с таким email уже существует");
            return ResponseEntity.badRequest().body(error);
        }

        if (userRepository.findByPhone(registerRequest.getPhone()).isPresent()) {
            Map<String, String> error = new HashMap<>();
            error.put("message", "Пользователь с таким телефоном уже существует");
            return ResponseEntity.badRequest().body(error);
        }

        // Создание нового пользователя
        User user = new User();
        user.setName(registerRequest.getName());
        user.setEmail(registerRequest.getEmail());
        user.setPhone(registerRequest.getPhone());
        user.setPassword(passwordEncoder.encode(registerRequest.getPassword()));
        user.setAddress(registerRequest.getAddress());
        user.setActive(true);

        // Назначение роли "Клиент" по умолчанию
        Role clientRole = roleRepository.findByName("Клиент")
                .orElseThrow(() -> new RuntimeException("Роль 'Клиент' не найдена"));
        user.setRole(clientRole);

        userRepository.save(user);

        Map<String, String> response = new HashMap<>();
        response.put("message", "Пользователь успешно зарегистрирован");
        response.put("email", user.getEmail());

        return ResponseEntity.status(HttpStatus.CREATED).body(response);
    }

    @Operation(summary = "Проверка токена", description = "Проверка валидности JWT токена")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Токен валиден"),
            @ApiResponse(responseCode = "401", description = "Токен невалиден или истек")
    })
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader("Authorization") String token) {
        try {
            String jwt = token.replace("Bearer ", "");
            boolean isValid = tokenProvider.validateToken(jwt);
            
            if (isValid) {
                String username = tokenProvider.getUsernameFromToken(jwt);
                Map<String, Object> response = new HashMap<>();
                response.put("valid", true);
                response.put("username", username);
                return ResponseEntity.ok(response);
            } else {
                Map<String, Object> response = new HashMap<>();
                response.put("valid", false);
                return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
            }
        } catch (Exception e) {
            Map<String, Object> response = new HashMap<>();
            response.put("valid", false);
            response.put("error", e.getMessage());
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(response);
        }
    }

    @Operation(summary = "Выход из системы", description = "Выход пользователя из системы")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Успешный выход")
    })
    @PostMapping("/logout")
    public ResponseEntity<?> logout() {
        SecurityContextHolder.clearContext();
        Map<String, String> response = new HashMap<>();
        response.put("message", "Успешный выход из системы");
        return ResponseEntity.ok(response);
    }
}




