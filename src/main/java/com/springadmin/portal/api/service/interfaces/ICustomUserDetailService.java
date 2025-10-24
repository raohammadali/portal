package com.springadmin.portal.api.service.interfaces;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.springadmin.portal.api.dto.request.SignupRequest;
import com.springadmin.portal.api.dto.request.UpdateRequest;
import com.springadmin.portal.core.model.User;

public interface ICustomUserDetailService {
    public User createUser(SignupRequest signupRequest);
    public Page<User> getUsers(Pageable pageable);
    public User editUser(UpdateRequest dto, String email);
    public boolean deleteUser(String email);
}