package com.yourcompany.usermanagement.user_management_service.Domain.exception;

public class LoginException extends EntityNotFound{

    public LoginException(String message) {
        super(message);
    }

    public LoginException(String message, Throwable cause) {
        super(message, cause);
    }
}
