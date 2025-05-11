package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to create a new user")
public record UserCreateRequest(

        @Schema(description = "Full name of the user", example = "Alice Smith")
        @NotBlank
        String name,

        @Schema(description = "Email address", example = "alice@example.com")
        @Email
        @NotBlank
        String email,

        @Schema(description = "Plain text password", example = "SecurePass123")
        @NotBlank
        String password

) {}
