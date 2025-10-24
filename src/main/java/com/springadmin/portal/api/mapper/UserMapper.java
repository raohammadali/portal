package com.springadmin.portal.api.mapper;

import org.springframework.stereotype.Component;

import com.springadmin.portal.api.dto.response.UserResponse;
import com.springadmin.portal.core.model.User;
import com.springadmin.portal.core.utils.HashIdUtil;

import java.util.ArrayList;
import java.util.List;

@Component
public class UserMapper {

    private final HashIdUtil hashIdUtil;
    private final RoleMapper roleMapper;
    private final PermissionMapper permissionMapper;

    public UserMapper(
        HashIdUtil hashIdUtil
    ) {
        this.hashIdUtil = hashIdUtil;
        this.roleMapper = new RoleMapper(hashIdUtil);
        this.permissionMapper = new PermissionMapper(hashIdUtil);
    }

    public UserResponse toUserResponse(User user) {
        if (user == null) return null;

        UserResponse dto = new UserResponse();
        dto.setId(hashIdUtil.encodeId(user.getId()));
        dto.setFirstName(user.getFirstName());
        dto.setLastName(user.getLastName());
        dto.setEmail(user.getEmail());
        dto.setPhone(user.getPhone());
        dto.setRoles(roleMapper.toRoleDtos(user.getRoles()));
        dto.setPermissions(permissionMapper.toPermissionDtos(user.getPermissions()));
        return dto;
    }

    public List<UserResponse> toUserResponses(List<User> users) {
        if (users.isEmpty()) {
            return new ArrayList<>();
        }
        return users.stream()
                .map(this::toUserResponse).toList();
    }
}
