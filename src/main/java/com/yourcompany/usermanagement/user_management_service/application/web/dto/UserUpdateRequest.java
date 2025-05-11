package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update user data")
public record UserUpdateRequest(

        @Schema(description = "Full name", example = "Alice Keys", minLength = 3, maxLength = 50)
        @NotBlank(message = "Name is required")
        @Size(min = 3, max = 50, message = "Name must be between 3 and 50 characters")
        String name,

        @Schema(description = "Email address", example = "alicekeys@example.com", format = "email")
        @NotBlank(message = "Email is required")
        @Email(message = "Email format is invalid")
        String email

) {}
