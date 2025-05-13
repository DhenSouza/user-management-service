package com.yourcompany.usermanagement.user_management_service.application.web.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;

public record AddresCepRequest(
    @Schema(description = "Postal code (CEP)", example = "01310-200")
    @NotBlank(message = "Postal code is required")
    @Pattern(regexp = "\\d{5}-\\d{3}", message = "Postal code must be in format 00000-000")
    String postalCode
) {}