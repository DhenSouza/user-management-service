package com.yourcompany.usermanagement.user_management_service.application.web.mapper;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;

import java.util.UUID;

public class UserResponse {

    private final UUID id;
    private final String name;
    private final String email;
    private final Role role;
    private final boolean active;

    public UserResponse(UUID id, String name, String email, Role role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public UUID getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getEmail() {
        return email;
    }

    public Role getRole() {
        return role;
    }

    public boolean isActive() {
        return active;
    }
}