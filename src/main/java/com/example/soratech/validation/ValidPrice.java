package com.example.soratech.validation;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

import java.lang.annotation.*;

@Documented
@Constraint(validatedBy = PriceValidator.class)
@Target({ElementType.FIELD, ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface ValidPrice {
    String message() default "Цена должна быть положительным числом больше 0";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}



