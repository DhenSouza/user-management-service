package com.yourcompany.usermanagement.user_management_service.application.service.address;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.interfaces.IAddressService;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
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
    public Address createAddress(UUID userId, AddressCreateRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        Address address = Address.builder()
                .street(request.street())
                .number(request.number())
                .complement(request.complement())
                .neighborhood(request.neighborhood())
                .city(request.city())
                .state(request.state())
                .postalCode(request.postalCode())
                .user(user)
                .build();

        return addressRepository.save(address);
    }

    @Transactional
    public void deleteAddress(UUID addressId) {
        addressRepository.deleteById(addressId);
    }

    @Transactional
    public Address updateAddress(UUID id, AddressUpdateRequest request) {
        Address address = addressRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found"));

        address.setStreet(request.street());
        address.setNumber(request.number());
        address.setComplement(request.complement());
        address.setNeighborhood(request.neighborhood());
        address.setCity(request.city());
        address.setState(request.state());
        address.setPostalCode(request.postalCode());

        return addressRepository.save(address);
    }

    public Optional<Address> getAddressById(UUID id) {
        return Optional.ofNullable(addressRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Address not found with ID: " + id)));
    }


}
