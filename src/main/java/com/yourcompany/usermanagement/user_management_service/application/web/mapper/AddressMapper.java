package com.yourcompany.usermanagement.user_management_service.application.web.mapper;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.AddressResponse;

public class AddressMapper {

    public static AddressResponse toResponse(Address address) {
        return new AddressResponse(
                address.getId(),
                address.getStreet(),
                address.getNumber(),
                address.getComplement(),
                address.getNeighborhood(),
                address.getCity(),
                address.getState(),
                address.getPostalCode(),
                address.getUser().getId()
        );
    }

}
