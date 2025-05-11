package com.yourcompany.usermanagement.user_management_service.application.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update the user's password")
public record PasswordUpdateRequest(

        @Schema(description = "User password", example = "SecureP@ssword1", minLength = 8, maxLength = 20)
        @NotBlank
        @Size(min = 8, max = 20, message = "Password must be between 8 and 20 characters")
        String password

) {}
