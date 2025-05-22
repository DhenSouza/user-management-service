package com.yourcompany.usermanagement.user_management_service.Domain.exception;

public abstract class EntityNotFound extends BusinessException {

    public EntityNotFound(String message) {
        super(message);
    }

    public EntityNotFound(String message, Throwable cause) {
        super(message, cause);
    }
}
