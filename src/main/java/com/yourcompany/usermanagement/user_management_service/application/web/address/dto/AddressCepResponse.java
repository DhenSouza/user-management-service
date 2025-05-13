package com.yourcompany.usermanagement.user_management_service.application.web.address.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.v3.oas.annotations.media.Schema;

public record AddressCepResponse(
    @Schema(description = "Postal code", example = "10001")
    String postalCode,

    @Schema(description = "Street name", example = "Main Street")
    String street,

    @Schema(description = "Neighborhood", example = "Downtown")
    String neighborhood,

    @Schema(description = "City", example = "New York")
    String city,

    @Schema(description = "State (2-letter code)", example = "NY")
    String state
) {
}
