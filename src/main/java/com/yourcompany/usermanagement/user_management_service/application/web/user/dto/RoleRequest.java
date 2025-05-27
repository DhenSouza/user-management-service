package com.yourcompany.usermanagement.user_management_service.application.web.user.dto;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public record RoleRequest (
        @Schema(description = "User Role", example = "USER", minLength = 4, maxLength = 5)
        @NotBlank(message = "Role is required")
        @Size(min = 4, max = 5, message = "Role must be between 4 and 5 characters")
        Role role) { }
