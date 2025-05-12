package com.yourcompany.usermanagement.user_management_service.application.web.login.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AuthResponse(

        @Schema(description = "JWT token generated for authentication", example = "Bearer eyJhbGciOiJIUzI1NiIsInR5...")
        String token
) {}

