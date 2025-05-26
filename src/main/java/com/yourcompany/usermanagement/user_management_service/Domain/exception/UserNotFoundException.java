package com.yourcompany.usermanagement.user_management_service.Domain.exception;

import java.util.UUID;

public class UserNotFoundException extends EntityNotFound{

    public UserNotFoundException(String message) {
        super(message);
    }

    public UserNotFoundException(UUID userId) {
        this(String.format("There is no user registration with the code %s", userId));

    }
}
