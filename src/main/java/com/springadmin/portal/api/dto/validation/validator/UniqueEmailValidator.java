package com.springadmin.portal.api.dto.validation.validator;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.validation.annotation.UniqueEmail;
import com.springadmin.portal.core.repositories.UserRepository;

@Component
public class UniqueEmailValidator implements ConstraintValidator<UniqueEmail, String> {

    @Autowired
    private UserRepository userRepository;

    @Override
    public boolean isValid(String email, ConstraintValidatorContext context) {
        if (email == null || email.trim().isEmpty()) {
            return true;
        }
        return !userRepository.existsByEmailIgnoreCase(email);
    }
}
