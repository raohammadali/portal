package com.springadmin.portal.core.exceptions;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.security.authorization.AuthorizationDeniedException;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import com.springadmin.portal.api.controller.BaseController;
import com.springadmin.portal.api.dto.response.ApiResponse;
import com.springadmin.portal.core.enums.CustomHttpStatus;



@ControllerAdvice
public class GlobalExceptionHandler extends BaseController {

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleValidationExceptions(
            MethodArgumentNotValidException ex) {
        Map<String, String> errors = new HashMap<>();
        ex.getBindingResult().getAllErrors().forEach(error -> {
        if (error instanceof FieldError fieldError) {
            errors.put(fieldError.getField(), fieldError.getDefaultMessage());
        } else {
            errors.put(error.getObjectName(), error.getDefaultMessage());
        }});
        return this.buildResponse(errors, false,
                HttpStatus.BAD_REQUEST, CustomHttpStatus.E_INVALID_INPUT);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ApiResponse<Map<String, String>>> handleMethodArgumentTypeMismatchException(MethodArgumentTypeMismatchException ex) {
        Map<String, String> errors = new HashMap<>();
        errors.put("error", Arrays.asList(ex.getMessage().split(";")).get(0).trim());
        return this.buildResponse(errors, false, HttpStatus.BAD_REQUEST, CustomHttpStatus.E_INVALID_INPUT);
    }

    @ExceptionHandler(EmailAlreadyUsedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleEmailAlreadyUsedException(EmailAlreadyUsedException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.CONFLICT,
                CustomHttpStatus.EMAIL_ALR_USED);
    }

    @ExceptionHandler(InvalidEmailPasswordException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleInvalidEmailPasswordException(
            InvalidEmailPasswordException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.BAD_REQUEST,
                CustomHttpStatus.F_LOGIN);
    }

    @ExceptionHandler(UserNotFoundException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleUserNotFoundException(UserNotFoundException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.NOT_FOUND,
                CustomHttpStatus.E_U_NOT_FOUND);
    }

    @ExceptionHandler(InvalidIDException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleInvalidIDException(InvalidIDException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.BAD_REQUEST,
                ex.getMessage().equals("role") ? CustomHttpStatus.E_INVALID_ROLE_ID
                        : ex.getMessage().equals("permission") ? CustomHttpStatus.E_INVALID_PERMISSION_ID
                                : ex.getMessage().equals("user") ? CustomHttpStatus.E_INVALID_USER_ID : CustomHttpStatus.E_INVALID_DISTRIBUTION_ID);
    }

    @ExceptionHandler(UnauthorizedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleUnauthorizedException(UnauthorizedException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.UNAUTHORIZED, CustomHttpStatus.E_UNAUTHORIZED);
    }

    @ExceptionHandler(InvalidJWTTokenException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleInvalidJWTTokenException(InvalidJWTTokenException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.BAD_REQUEST, CustomHttpStatus.E_INVALID_TOKEN);
    }

    @ExceptionHandler(UsernameNotFoundException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleUsernameNotFoundException(UsernameNotFoundException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.NOT_FOUND,
                CustomHttpStatus.E_USERNAME_NOT_FOUND);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleHttpMessageNotReadableException(
            HttpMessageNotReadableException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.BAD_REQUEST, CustomHttpStatus.E_INVALID_INPUT);
    }

    @ExceptionHandler(AuthorizationDeniedException.class)
    public ResponseEntity<ApiResponse<List<String>>> handleAuthorizationDeniedException(
            AuthorizationDeniedException ex) {
        return this.buildResponse(new ArrayList<>(), false, HttpStatus.BAD_REQUEST, CustomHttpStatus.E_UNAUTHORIZED);
    }
}
