package com.yourcompany.usermanagement.user_management_service.application.service;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.interfaces.IAddressService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class AddressService implements IAddressService {

    private final IAddressRepository addressRepository;
    private final IUserRepository userRepository;

    public List<Address> getAddressesByUserId(UUID userId) {
        return addressRepository.findAllByUserId(userId);
    }

    @Transactional
    public Address createAddress(UUID userId, Address address) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        address.setUser(user);
        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

}
