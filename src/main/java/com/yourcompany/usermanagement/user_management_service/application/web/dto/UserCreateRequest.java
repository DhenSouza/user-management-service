package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a new user")
public record UserCreateRequest(

        @Schema(description = "Full name", example = "Alice Smith", minLength = 3, maxLength = 20)
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 20, message = "Name must be between 3 and 50 characters")
        String name,

        @Schema(description = "Email address", example = "alice@example.com", format = "email")
        @Email(message = "Email format is invalid")
        @NotBlank(message = "Email is required")
        String email,

        @Schema(description = "User password", example = "SecureP@ssword1", minLength = 8, maxLength = 20)
        @NotBlank(message = "Password is required")
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password

) {}
