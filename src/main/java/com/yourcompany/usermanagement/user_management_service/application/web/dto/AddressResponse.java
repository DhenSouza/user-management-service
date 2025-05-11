package com.yourcompany.usermanagement.user_management_service.application.web.dto;

import java.util.UUID;

public class AddressResponse {
    private final UUID id;
    private final String street;
    private final String number;
    private final String complement;
    private final String neighborhood;
    private final String city;
    private final String state;
    private final String postalCode;
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

    public UUID getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getNumber() {
        return number;
    }

    public String getComplement() {
        return complement;
    }

    public String getNeighborhood() {
        return neighborhood;
    }

    public String getCity() {
        return city;
    }

    public String getState() {
        return state;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public UUID getUserId() {
        return userId;
    }
}
