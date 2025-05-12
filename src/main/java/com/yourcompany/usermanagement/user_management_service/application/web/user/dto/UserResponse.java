package com.yourcompany.usermanagement.user_management_service.application.web.user.dto;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "User details returned in responses")
public class UserResponse {

    @Schema(description = "User ID", example = "c8f51e1a-2f7c-42fb-9012-b42a274c7a90")
    private final UUID id;

    @Schema(description = "Full name of the user", example = "Alice Smith")
    private final String name;

    @Schema(description = "Email address", example = "Alice@example.com")
    private final String email;

    @Schema(description = "User role", example = "USER")
    private final Role role;

    @Schema(description = "Whether the user is active", example = "true")
    private final boolean active;

    public UserResponse(UUID id, String name, String email, Role role, boolean active) {
        this.id = id;
        this.name = name;
        this.email = email;
        this.role = role;
        this.active = active;
    }

    public UUID getId() { return id; }
    public String getName() { return name; }
    public String getEmail() { return email; }
    public Role getRole() { return role; }
    public boolean isActive() { return active; }
}