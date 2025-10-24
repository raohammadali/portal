package com.springadmin.portal.api.controller;
import java.security.AuthProvider;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import com.springadmin.portal.api.dto.request.LoginRequest;
import com.springadmin.portal.api.dto.request.SignupRequest;
import com.springadmin.portal.api.dto.request.UpdateRequest;
import com.springadmin.portal.api.dto.request.UpdateUserRequest;
import com.springadmin.portal.api.dto.request.UserSearchRequest;
import com.springadmin.portal.api.dto.response.ApiResponse;
import com.springadmin.portal.api.dto.response.LoginResponse;
import com.springadmin.portal.api.dto.response.UserResponse;
import com.springadmin.portal.api.mapper.UserMapper;
import com.springadmin.portal.api.service.interfaces.IAuthenticationService;
import com.springadmin.portal.api.service.interfaces.ICustomUserDetailService;
import com.springadmin.portal.core.enums.CustomHttpStatus;
import com.springadmin.portal.core.exceptions.InvalidIDException;
import com.springadmin.portal.core.model.User;
import com.springadmin.portal.core.utils.HashIdUtil;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class AuthenticationController extends BaseController {
    
    private final ICustomUserDetailService userService;
    private final IAuthenticationService authService;

    private final SecurityContextLogoutHandler logoutHandler = new SecurityContextLogoutHandler();

    private final HashIdUtil hashIdUtil;
    private final UserMapper userMapper;

    public AuthenticationController(ICustomUserDetailService userService, HashIdUtil hashIdUtil, IAuthenticationService authenticationService, UserMapper userMapper) {
        this.userService = userService;
        this.hashIdUtil = hashIdUtil;
        this.authService = authenticationService;
        this.userMapper = userMapper;
    }
    @PostMapping("/login")
    public ResponseEntity<ApiResponse<LoginResponse>> login(@RequestBody @Valid LoginRequest loginRequest) {
        LoginResponse loginResponse = authService.authenticate(loginRequest.getEmail(),
                loginRequest.getPassword());
        return this.buildResponse(loginResponse,
                true,
                HttpStatus.OK, CustomHttpStatus.S_LOGIN);
    }
    @PostMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(@Valid @RequestBody SignupRequest createUserRequest) {
        User user = userService.createUser(createUserRequest);
        return this.buildResponse(userMapper.toUserResponse(user), true,
                HttpStatus.OK, CustomHttpStatus.S_SIGNUP);
    }
    @GetMapping("/all-users")
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getAllUsers(UserSearchRequest request) {

        Sort.Direction sortDirection = Sort.Direction.fromOptionalString(request.getDirection())
                .orElse(Sort.Direction.ASC);
        Pageable pageable = PageRequest.of(
                request.getPage(),
                request.getSize(),
                Sort.by(sortDirection, request.getSortBy())
        );

        Page<UserResponse> users = userService.getUsers(pageable).map(userMapper::toUserResponse);

        return buildResponse(users, true, HttpStatus.OK, CustomHttpStatus.S_FETCH_U);
    }

    @PutMapping("/user")
    public ResponseEntity<ApiResponse<UserResponse>> editUser(@RequestBody @Valid UpdateRequest dto) {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            return this.buildResponse(userMapper.toUserResponse(userService.editUser(dto,email)), true,
                    HttpStatus.OK, CustomHttpStatus.S_UPDATE);
        } catch (IllegalArgumentException e) {
            throw new InvalidIDException("user");
        }
    }

    @DeleteMapping("/user")
    public ResponseEntity<ApiResponse<Boolean>> deleteUser() {
        try {
            String email = SecurityContextHolder.getContext().getAuthentication().getPrincipal().toString();
            return this.buildResponse(userService.deleteUser(email), true,
                    HttpStatus.OK, CustomHttpStatus.S_UPDATE);
        } catch (IllegalArgumentException e) {
            throw new InvalidIDException("user");
        }
    }

}
