package com.example.soratech.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;
import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PasswordValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPassword {
    String message() default "Пароль должен содержать минимум 8 символов, включая заглавные буквы, цифры и специальные символы";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}







