package ru.zeker.user_service.exception;

import org.springframework.http.HttpStatus;
import ru.zeker.common.exception.ApiException;

public class UserAlreadyEnableException extends ApiException {
    public UserAlreadyEnableException(String message) {
        super(message, HttpStatus.CONFLICT);
    }
    public UserAlreadyEnableException() {
        super("User already enable", HttpStatus.CONFLICT);
    }
}
