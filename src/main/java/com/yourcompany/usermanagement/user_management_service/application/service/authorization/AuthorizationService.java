package com.yourcompany.usermanagement.user_management_service.application.service.authorization;

import com.yourcompany.usermanagement.user_management_service.Domain.exception.UserNotFoundException;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.authorization.interfaces.IAuthorizationService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class AuthorizationService implements IAuthorizationService {

    private final IUserRepository userRepository;

    public void ensureAdmin() {
        var auth = getAuthentication();
        if (!isAdmin(auth)) {
            throw new AccessDeniedException("Access denied: Admin role required.");
        }
    }

    public void ensureSelfOrAdmin(UUID userId) {
        var auth = getAuthentication();
        if (isAdmin(auth)) return;

        User user = userRepository.findById(userId)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + userId));

        if(!user.isActive()){
            throw new AccessDeniedException("Access denied: Your user is inactive.");
        }

        if (!user.getEmail().equals(auth.getName())) {
            throw new AccessDeniedException("Access denied: Not your data.");
        }
    }

    public void ensureSelfOrAdmin(String email) {
        var auth = getAuthentication();

        if (!isAdmin(auth) && !auth.getName().equals(email)) {
            throw new AccessDeniedException("Access denied: Not your data.");
        }
    }

    private Authentication getAuthentication() {
        var auth = SecurityContextHolder.getContext().getAuthentication();
        if (auth == null || !auth.isAuthenticated()) {
            throw new AccessDeniedException("No authentication found.");
        }
        return auth;
    }

    private boolean isAdmin(Authentication auth) {
        return auth.getAuthorities().stream()
                .anyMatch(a -> a.getAuthority().equals("ROLE_ADMIN"));
    }
}
