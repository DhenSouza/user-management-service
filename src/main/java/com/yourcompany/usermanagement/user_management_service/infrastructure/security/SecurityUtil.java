package com.yourcompany.usermanagement.user_management_service.infrastructure.security;

import org.springframework.security.core.context.SecurityContextHolder;

public class SecurityUtil {
    public static String getAuthenticatedEmail() {
        return SecurityContextHolder.getContext().getAuthentication().getName();
    }
}

