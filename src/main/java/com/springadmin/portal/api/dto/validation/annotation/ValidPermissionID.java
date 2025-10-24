package com.springadmin.portal.api.dto.validation.annotation;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

import com.springadmin.portal.api.dto.validation.validator.ValidPermissionIDValidator;

import jakarta.validation.Constraint;
import jakarta.validation.Payload;

@Target({ElementType.TYPE})
@Retention(RetentionPolicy.RUNTIME)
@Constraint(validatedBy = ValidPermissionIDValidator.class)
public @interface ValidPermissionID {
    String message() default "Invalid Permission IDs";
    Class<?>[] groups() default {};
    Class<? extends Payload>[] payload() default {};
}
