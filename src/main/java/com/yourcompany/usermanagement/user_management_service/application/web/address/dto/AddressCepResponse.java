package com.yourcompany.usermanagement.user_management_service.application.web.address.dto;

import io.swagger.v3.oas.annotations.media.Schema;

public record AddressCepResponse(

        @Schema(description = "Postal code", example = "01001-000")
        String postalCode,

        @Schema(description = "Street name", example = "Praça da Sé")
        String street,

        @Schema(description = "Complement", example = "lado ímpar")
        String complement,

        @Schema(description = "Neighborhood", example = "Sé")
        String neighborhood,

        @Schema(description = "City", example = "São Paulo")
        String city,

        @Schema(description = "State (2-letter code)", example = "SP")
        String state,

        @Schema(description = "IBGE code of the city", example = "3550308")
        String ibgeCode,

        @Schema(description = "GIA code", example = "1004")
        String giaCode,

        @Schema(description = "Telephone DDD code", example = "11")
        String ddd,

        @Schema(description = "SIAFI code", example = "7107")
        String siafiCode

) {}