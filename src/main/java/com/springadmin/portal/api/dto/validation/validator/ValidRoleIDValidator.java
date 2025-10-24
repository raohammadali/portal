package com.springadmin.portal.api.dto.validation.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.request.SignupRequest;
import com.springadmin.portal.api.dto.request.UpdateRequest;
import com.springadmin.portal.api.dto.validation.annotation.ValidRoleID;
import com.springadmin.portal.core.model.Role;
import com.springadmin.portal.core.repositories.RoleRepository;
import com.springadmin.portal.core.utils.HashIdUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidRoleIDValidator implements ConstraintValidator<ValidRoleID, Object>{

    private final HashIdUtil hashIdUtil;
    private final RoleRepository roleRepository;

    public ValidRoleIDValidator(HashIdUtil hashIdUtil, RoleRepository roleRepository) {
        this.hashIdUtil = hashIdUtil;
        this.roleRepository = roleRepository;
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        record RoleData(List<String> roleIds) {
        }

        RoleData data = null;
        if (obj instanceof SignupRequest) {
            data = new RoleData(((SignupRequest) obj).getRoles());
        } else if (obj instanceof UpdateRequest) {
            data = new RoleData(((UpdateRequest) obj).getRoles());
        }
        if (data == null) return false;

        if (data.roleIds().isEmpty() || data.roleIds() == null) {
            return true;
        }
        try {
            List<String> value = data.roleIds();
            List<Role> roles = roleRepository
                        .findByIdIn(value.stream().map(hashIdUtil::decodeId).toList());
            return roles.size() == value.size();
        } catch (Exception e) {
            return false;
        }
    }
}
