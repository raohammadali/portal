package com.springadmin.portal.api.dto.response;
import java.util.List;

import com.springadmin.portal.api.dto.dto.PermissionDto;
import com.springadmin.portal.api.dto.dto.RoleDto;

import lombok.Data;
@Data
public class UserResponse {
    private String id;
    private String firstName;
    private String lastName;
    private String email;
    private String phone;
    private List<RoleDto> roles;
    private List<PermissionDto> permissions;
}

