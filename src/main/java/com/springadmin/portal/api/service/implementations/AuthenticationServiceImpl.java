package com.springadmin.portal.api.service.implementations;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springadmin.portal.api.dto.response.LoginResponse;
import com.springadmin.portal.api.mapper.PermissionMapper;
import com.springadmin.portal.api.service.interfaces.IAuthenticationService;
import com.springadmin.portal.core.exceptions.InvalidEmailPasswordException;
import com.springadmin.portal.core.exceptions.UserNotFoundException;
import com.springadmin.portal.core.model.User;
import com.springadmin.portal.core.repositories.UserRepository;
import com.springadmin.portal.core.utils.JwtUtil;


@Service
public class AuthenticationServiceImpl implements IAuthenticationService {
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;
    private final PermissionMapper permissionMapper;

    public AuthenticationServiceImpl(UserRepository userRepository, JwtUtil jwtUtil,
            PasswordEncoder passwordEncoder,PermissionMapper permissionMapper) {
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
        this.permissionMapper = permissionMapper;
    }

    public LoginResponse authenticate(String email, String password) {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(UserNotFoundException::new);
        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new InvalidEmailPasswordException();
        }
        String token = jwtUtil.generateToken(user, null);
        return new LoginResponse(token, user.getEmail(), null, permissionMapper.toPermissionDtos(user.getPermissions()));
    }

}
