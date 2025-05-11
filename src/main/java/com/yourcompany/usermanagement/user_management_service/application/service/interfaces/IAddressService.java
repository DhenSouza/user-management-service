package com.yourcompany.usermanagement.user_management_service.application.service.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;

import java.util.List;
import java.util.UUID;

public interface IAddressService {
    Address createAddress(UUID userId, Address address);

    List<Address> getAddressesByUserId(UUID userId);

    void deleteAddress(UUID addressId);
}
