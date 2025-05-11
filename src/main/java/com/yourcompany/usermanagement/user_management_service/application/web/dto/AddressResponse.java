package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.UUID;

@Schema(description = "Address details returned in responses")
public class AddressResponse {

    @Schema(description = "Address ID", example = "e983272b-3aef-4571-b9c8-35d2e579a24a")
    private final UUID id;

    @Schema(description = "Street name", example = "Main Street")
    private final String street;

    @Schema(description = "Number", example = "123")
    private final String number;

    @Schema(description = "Complement (optional)", example = "Apt 4B")
    private final String complement;

    @Schema(description = "Neighborhood", example = "Downtown")
    private final String neighborhood;

    @Schema(description = "City", example = "New York")
    private final String city;

    @Schema(description = "State (2-letter code)", example = "NY")
    private final String state;

    @Schema(description = "Postal code", example = "10001")
    private final String postalCode;

    @Schema(description = "ID of the user that owns the address", example = "b7f98d47-0f52-446b-a70f-c67651f298b6")
    private final UUID userId;

    public AddressResponse(UUID id, String street, String number, String complement, String neighborhood,
                           String city, String state, String postalCode, UUID userId) {
        this.id = id;
        this.street = street;
        this.number = number;
        this.complement = complement;
        this.neighborhood = neighborhood;
        this.city = city;
        this.state = state;
        this.postalCode = postalCode;
        this.userId = userId;
    }

    public UUID getId() { return id; }
    public String getStreet() { return street; }
    public String getNumber() { return number; }
    public String getComplement() { return complement; }
    public String getNeighborhood() { return neighborhood; }
    public String getCity() { return city; }
    public String getState() { return state; }
    public String getPostalCode() { return postalCode; }
    public UUID getUserId() { return userId; }
}
