package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to update an existing address")
public record AddressUpdateRequest(

        @Schema(description = "Street name", example = "Rua das Flores")
        @NotBlank(message = "Street is required")
        String street,

        @Schema(description = "Number", example = "123")
        @NotBlank(message = "Number is required")
        String number,

        @Schema(description = "Complement (optional)", example = "Fundos")
        String complement,

        @Schema(description = "Neighborhood", example = "Centro")
        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

        @Schema(description = "City", example = "Rio de Janeiro")
        @NotBlank(message = "City is required")
        String city,

        @Schema(description = "State (2-letter code)", example = "RJ")
        @NotBlank(message = "State is required")
        @Size(min = 2, max = 2, message = "State must be a 2-letter code")
        String state,

        @Schema(description = "Postal code (CEP)", example = "20000-000")
        @NotBlank(message = "Postal code is required")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Postal code must be in format 00000-000")
        String postalCode

) {}