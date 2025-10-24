package com.springadmin.portal.api.dto.dto;

import java.util.List;


import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class RoleDto {
    private String id;
    private String name;
    private List<PermissionDto> permissions;
}
