package com.springadmin.portal.api.mapper;

import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.dto.PermissionDto;
import com.springadmin.portal.core.model.Permission;
import com.springadmin.portal.core.utils.HashIdUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class PermissionMapper {

    private final HashIdUtil hashIdUtil;

    public PermissionMapper(HashIdUtil hashIdUtil) {
        this.hashIdUtil = hashIdUtil;
    }

    public PermissionDto toPermissionDto(Permission permission) {
        if (permission == null) return null;
        return new PermissionDto(hashIdUtil.encodeId(permission.getId()), permission.getName());
    }

    public List<PermissionDto> toPermissionDtos(List<Permission> permissions) {
        if (permissions.isEmpty()) {
            return new ArrayList<>();
        }
        return permissions.stream()
                .map(this::toPermissionDto)
                .toList();
    }
}
