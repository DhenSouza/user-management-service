package com.yourcompany.usermanagement.user_management_service.application.web.errorHandler;

public class ResourceNotFoundException extends RuntimeException {
    public ResourceNotFoundException(String message) {
        super(message);
    }
}
