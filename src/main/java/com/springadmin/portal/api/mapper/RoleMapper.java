package com.springadmin.portal.api.mapper;

import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.dto.RoleDto;
import com.springadmin.portal.core.model.Role;
import com.springadmin.portal.core.utils.HashIdUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class RoleMapper {

    private final HashIdUtil hashIdUtil;

    public RoleMapper(HashIdUtil hashIdUtil) {
        this.hashIdUtil = hashIdUtil;
    }

    public RoleDto toRoleDto(Role role) {
        if (role == null) return null;
        PermissionMapper permissionMapper = new PermissionMapper(hashIdUtil);
        return new RoleDto(hashIdUtil.encodeId(role.getId()), role.getName(), permissionMapper.toPermissionDtos(role.getPermissions()));
    }

    public List<RoleDto> toRoleDtos(List<Role> roles) {
        if (roles.isEmpty()) {
            return new ArrayList<>();
        }
        return roles.stream()
                .map(this::toRoleDto).toList();
    }
}

