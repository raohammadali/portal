package com.springadmin.portal.api.service.implementations;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.boot.actuate.autoconfigure.metrics.MetricsProperties.Distribution;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.springadmin.portal.api.dto.request.SignupRequest;
import com.springadmin.portal.api.dto.request.UpdateRequest;
import com.springadmin.portal.api.service.interfaces.ICustomUserDetailService;
import com.springadmin.portal.core.exceptions.EmailAlreadyUsedException;
import com.springadmin.portal.core.exceptions.UserNotFoundException;
import com.springadmin.portal.core.model.Permission;
import com.springadmin.portal.core.model.Role;
import com.springadmin.portal.core.model.User;
import com.springadmin.portal.core.repositories.PermissionRepository;
import com.springadmin.portal.core.repositories.RoleRepository;
import com.springadmin.portal.core.repositories.UserRepository;
import com.springadmin.portal.core.utils.HashIdUtil;

import jakarta.transaction.Transactional;

@Service
public class CustomUserDetailsServiceImpl implements UserDetailsService, ICustomUserDetailService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;
    private final PermissionRepository permissionRepository;
    private final HashIdUtil hashIdUtil;

    

    public CustomUserDetailsServiceImpl(
            UserRepository userRepository,
            RoleRepository roleRepository,
            PermissionRepository permissionRepository,
            PasswordEncoder passwordEncoder,
            HashIdUtil hashIdUtil) {
        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.permissionRepository = permissionRepository;
        this.passwordEncoder = passwordEncoder;
        this.hashIdUtil = hashIdUtil;
    }

    @Override
    public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
        User user = userRepository.findByEmailIgnoreCase(email)
                .orElseThrow(() -> new UsernameNotFoundException(""));

        Set<GrantedAuthority> authorities = user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority(role.getName()))
                .collect(Collectors.toSet());

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                authorities);
    }

    public Page<User> getUsers(Pageable pageable){
        return userRepository.findAll(pageable);
    }
    @Transactional
    public User createUser(SignupRequest signupRequest) {
        if (userRepository.existsByEmail(signupRequest.getEmail())) {
                throw new EmailAlreadyUsedException();
        }
        List<Long> roleIds = signupRequest.getRoles();
        List<Role> roles = roleRepository.findAllById(roleIds);
        User user = new User(
                signupRequest.getFirstName(),
                signupRequest.getLastName(),
                signupRequest.getPhone(),
                signupRequest.getEmail(),
                passwordEncoder.encode(signupRequest.getPassword()),
                roles
        );
        List<Permission> permissions = (signupRequest.getPermissions() == null || signupRequest.getPermissions().isEmpty())
                ? roles.stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .distinct()
                        .toList()
                : permissionRepository.findAllById(
                        signupRequest.getPermissions().stream()
                                .map(hashIdUtil::decodeId)
                                .toList()
                );
        user.setPermissions(permissions);
        return userRepository.save(user);
        }
        public User editUser(UpdateRequest dto,String email) {
                User user = userRepository.findByEmailIgnoreCase(email)
                        .orElseThrow(UserNotFoundException::new);

                if (userRepository.existsByEmail(dto.getEmail()) && !user.getEmail().equals(dto.getEmail())) {
                throw new EmailAlreadyUsedException();
                }

                user.setFirstName(dto.getFirstName());
                user.setLastName(dto.getLastName());
                user.setEmail(dto.getEmail());
                user.setPhone(dto.getPhone());

                List<Role> roles = roleRepository.findAllById(dto.getRoles().stream().map(hashIdUtil::decodeId).toList());
                List<Permission> permissions;

                if (dto.getPermissions() == null || dto.getPermissions().isEmpty()) {
                permissions = roles.stream()
                        .flatMap(role -> role.getPermissions().stream())
                        .toList();
                } else {
                permissions = permissionRepository
                        .findAllById(dto.getPermissions().stream().map(hashIdUtil::decodeId).toList());
                }

                user.setRoles(roles);
                user.setPermissions(permissions);
                return userRepository.save(user);
        }

        public boolean deleteUser(String email){
                User user = userRepository.findByEmailIgnoreCase(email)
                        .orElseThrow(UserNotFoundException::new);
                userRepository.delete(user);
                return true;
        }

}
