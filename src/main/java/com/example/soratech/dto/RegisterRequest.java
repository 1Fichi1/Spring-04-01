package com.example.soratech.dto;

import com.example.soratech.validation.ValidPassword;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Данные для регистрации нового пользователя")
public class RegisterRequest {
    
    @Schema(description = "Имя пользователя", example = "Иван Иванов", required = true)
    @NotBlank(message = "Имя обязательно для заполнения")
    @Size(min = 2, max = 50, message = "Имя должно содержать от 2 до 50 символов")
    private String name;
    
    @Schema(description = "Email пользователя", example = "test@test.com", required = true)
    @NotBlank(message = "Email обязателен для заполнения")
    @Email(message = "Некорректный формат email")
    private String email;
    
    @Schema(description = "Телефон пользователя", example = "+79001234567", required = true)
    @Pattern(regexp = "^\\+?[0-9]{10,15}$", message = "Некорректный формат телефона")
    private String phone;
    
    @Schema(description = "Пароль (минимум 8 символов, 1 заглавная, 1 строчная, 1 цифра, 1 спецсимвол)", 
            example = "Test@123", required = true)
    @NotBlank(message = "Пароль обязателен для заполнения")
    @ValidPassword
    private String password;
    
    @Schema(description = "Подтверждение пароля", example = "Test@123", required = true)
    @NotBlank(message = "Подтверждение пароля обязательно")
    private String confirmPassword;
    
    @Schema(description = "Адрес доставки", example = "Москва, ул. Ленина, д. 1")
    private String address;

    // Getters and Setters
    public String getName() { return name; }
    public void setName(String name) { this.name = name; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPhone() { return phone; }
    public void setPhone(String phone) { this.phone = phone; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public String getConfirmPassword() { return confirmPassword; }
    public void setConfirmPassword(String confirmPassword) { this.confirmPassword = confirmPassword; }

    public String getAddress() { return address; }
    public void setAddress(String address) { this.address = address; }
}

