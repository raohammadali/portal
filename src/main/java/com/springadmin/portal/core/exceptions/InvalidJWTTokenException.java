package com.springadmin.portal.core.exceptions;

public class InvalidJWTTokenException extends RuntimeException {
    
    private final String errorCode;

    public InvalidJWTTokenException() {
        super("Invalid JWT token");
        this.errorCode = "INVALID_TOKEN";
    }

    public InvalidJWTTokenException(String message) {
        super(message);
        this.errorCode = "INVALID_TOKEN";
    }

    public InvalidJWTTokenException(String message, Throwable cause) {
        super(message, cause);
        this.errorCode = "INVALID_TOKEN";
    }

    public InvalidJWTTokenException(String message, String errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public String getErrorCode() {
        return errorCode;
    }
}