package com.yourcompany.usermanagement.user_management_service.application.service.authorization.interfaces;

import org.springframework.security.core.Authentication;

import java.util.UUID;

public interface IAuthorizationService {
    void ensureAdmin();
    void ensureSelfOrAdmin(UUID userId);
    void ensureSelfOrAdmin(String email);

}
