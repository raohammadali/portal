package com.springadmin.portal.api.dto.response;
import java.util.*;

import com.springadmin.portal.api.dto.dto.PermissionDto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class LoginResponse {
    private String token;
    private String email;
    private String refreshToken;
    private List<PermissionDto> permissions;
}
