package com.springadmin.portal.api.controller;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import com.springadmin.portal.api.dto.response.ApiResponse;
import com.springadmin.portal.core.enums.CustomHttpStatus;



public abstract class BaseController {

    protected <T> ResponseEntity<ApiResponse<T>> buildResponse(T data, boolean success, HttpStatus status, CustomHttpStatus customStatus) {
        ApiResponse<T> response = new ApiResponse<>(success, data, customStatus.value());
        return ResponseEntity.status(status).body(response);
    }
}


