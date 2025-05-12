package com.yourcompany.usermanagement.user_management_service.application.service.address.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IAddressService {
    Address createAddress(UUID userId, AddressCreateRequest address);

    List<Address> getAddressesByUserId(UUID userId);

    void deleteAddress(UUID addressId);

    Address updateAddress(UUID id, AddressUpdateRequest request);

    Optional<Address> getAddressById(UUID id);

}
