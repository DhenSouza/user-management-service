package com.yourcompany.usermanagement.user_management_service.application.web.address.dto;


import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;

@Schema(description = "Payload to create a new address")
public record AddressCreateRequest(

        @Schema(description = "Street name", example = "Av. Paulista")
        @NotBlank(message = "Street is required")
        String street,

        @Schema(description = "Number", example = "1578")
        @NotBlank(message = "Number is required")
        String number,

        @Schema(description = "Complement (optional)", example = "Apto 101")
        String complement,

        @Schema(description = "Neighborhood", example = "Bela Vista")
        @NotBlank(message = "Neighborhood is required")
        String neighborhood,

        @Schema(description = "City", example = "São Paulo")
        @NotBlank(message = "City is required")
        String city,

        @Schema(description = "State (2-letter code)", example = "SP", minLength = 2, maxLength = 2)
        @NotBlank(message = "State is required")
        @Size(min = 2, max = 2, message = "State must be a 2-letter code")
        String state,

        @Schema(description = "Postal code (CEP)", example = "01310-200")
        @NotBlank(message = "Postal code is required")
        @Pattern(regexp = "\\d{5}-\\d{3}", message = "Postal code must be in format 00000-000")
        String postalCode

) {}