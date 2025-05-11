package com.yourcompany.usermanagement.user_management_service.application.web.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;

@Schema(description = "Payload to update the user's password")
public record PasswordUpdateRequest(

        @Schema(description = "New password", example = "MyNewP@ssword")
        @NotBlank
        String password

) {}
