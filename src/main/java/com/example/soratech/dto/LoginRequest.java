package com.example.soratech.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Данные для входа в систему")
public class LoginRequest {
    
    @Schema(description = "Email или телефон пользователя", example = "test@test.com", required = true)
    @NotBlank(message = "Email или телефон обязательны для заполнения")
    private String username; // может быть email или телефон
    
    @Schema(description = "Пароль пользователя", example = "Test@123", required = true)
    @NotBlank(message = "Пароль обязателен для заполнения")
    private String password;
    
    @Schema(description = "Запомнить пользователя", example = "false")
    private boolean rememberMe;

    // Getters and Setters
    public String getUsername() { return username; }
    public void setUsername(String username) { this.username = username; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public boolean isRememberMe() { return rememberMe; }
    public void setRememberMe(boolean rememberMe) { this.rememberMe = rememberMe; }
}

