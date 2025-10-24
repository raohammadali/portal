package com.springadmin.portal.api.dto.validation.validator;

import java.util.List;

import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.request.SignupRequest;
import com.springadmin.portal.api.dto.request.UpdateRequest;
import com.springadmin.portal.api.dto.validation.annotation.ValidPermissionID;
import com.springadmin.portal.core.model.Permission;
import com.springadmin.portal.core.repositories.PermissionRepository;
import com.springadmin.portal.core.utils.HashIdUtil;

import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

@Component
public class ValidPermissionIDValidator implements ConstraintValidator<ValidPermissionID, Object> {

    private final HashIdUtil hashIdUtil;
    private final PermissionRepository permissionRepository;

    public ValidPermissionIDValidator(HashIdUtil hashIdUtil, PermissionRepository permissionRepository) {
        this.hashIdUtil = hashIdUtil;
        this.permissionRepository = permissionRepository;
    }

    @Override
    public boolean isValid(Object obj, ConstraintValidatorContext context) {
        record PermissionData(List<String> permissionIds) {}

        PermissionData data;
        if (obj instanceof SignupRequest request) {
            data = new PermissionData(request.getPermissions());
        } else if (obj instanceof UpdateRequest request) {
            data = new PermissionData(request.getPermissions());
        } else {
            data = null;
        }
        if (data == null)
            return false;
        if (data.permissionIds() == null || data.permissionIds().isEmpty()) {
            return true;
        }

        try {
            List<Long> decodedIds = data.permissionIds.stream()
                    .map(hashIdUtil::decodeId)
                    .toList();

            List<Permission> permissions = permissionRepository.findByIdIn(decodedIds);
            return permissions.size() == decodedIds.size();
        } catch (IllegalArgumentException e) {
            return false;
        }
    }
}
