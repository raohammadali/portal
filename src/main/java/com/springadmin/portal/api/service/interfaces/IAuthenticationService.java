package com.springadmin.portal.api.service.interfaces;

import com.springadmin.portal.api.dto.response.LoginResponse;

public interface IAuthenticationService {

    public LoginResponse authenticate(String email, String password);

}
