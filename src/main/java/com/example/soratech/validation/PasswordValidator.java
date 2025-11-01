package com.example.soratech.validation;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class PasswordValidator implements ConstraintValidator<ValidPassword, String> {

    @Override
    public void initialize(ValidPassword constraintAnnotation) {
    }

    @Override
    public boolean isValid(String password, ConstraintValidatorContext context) {
        if (password == null || password.isEmpty()) {
            return false;
        }

        // Минимум 8 символов
        if (password.length() < 8) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать минимум 8 символов")
                   .addConstraintViolation();
            return false;
        }

        // Проверка на наличие заглавной буквы
        if (!password.matches(".*[A-ZА-Я].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну заглавную букву")
                   .addConstraintViolation();
            return false;
        }

        // Проверка на наличие строчной буквы
        if (!password.matches(".*[a-zа-я].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну строчную букву")
                   .addConstraintViolation();
            return false;
        }

        // Проверка на наличие цифры
        if (!password.matches(".*\\d.*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы одну цифру")
                   .addConstraintViolation();
            return false;
        }

        // Проверка на наличие специального символа
        if (!password.matches(".*[!@#$%^&*()_+\\-=\\[\\]{};':\"\\\\|,.<>\\/?].*")) {
            context.disableDefaultConstraintViolation();
            context.buildConstraintViolationWithTemplate("Пароль должен содержать хотя бы один специальный символ (!@#$%^&* и т.д.)")
                   .addConstraintViolation();
            return false;
        }

        return true;
    }
}







