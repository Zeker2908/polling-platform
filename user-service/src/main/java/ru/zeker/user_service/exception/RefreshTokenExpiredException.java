package ru.zeker.user_service.exception;

import org.springframework.http.HttpStatus;

public class RefreshTokenExpiredException extends ApiException {
    public RefreshTokenExpiredException(String message, HttpStatus status) {
        super(message, status);
    }
    public RefreshTokenExpiredException() {
        super("Refresh token expired", HttpStatus.UNAUTHORIZED);
    }
}
