package com.yourcompany.usermanagement.user_management_service.unitTest;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.address.AddressService;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

public class AddressServiceTest {

    @Mock private IAddressRepository addressRepository;
    @Mock private IUserRepository userRepository;
    @InjectMocks private AddressService addressService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        UUID userId = UUID.randomUUID();
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        AddressCreateRequest request = new AddressCreateRequest("Rua A", "123", "Apto 1", "Bairro", "Cidade", "SP", "12345-000");
        when(addressRepository.save(any(Address.class))).thenAnswer(i -> i.getArgument(0));

        Address result = addressService.createAddress(userId, request);

        assertThat(result.getStreet()).isEqualTo("Rua A");
        assertThat(result.getUser()).isEqualTo(user);
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        UUID addressId = UUID.randomUUID();
        Address existing = new Address();
        existing.setId(addressId);

        when(addressRepository.findById(addressId)).thenReturn(Optional.of(existing));
        when(addressRepository.save(any(Address.class))).thenAnswer(invocation -> invocation.getArgument(0));

        AddressUpdateRequest updateRequest = new AddressUpdateRequest("Nova", "1", "", "Bairro", "Cidade", "SP", "00000-000");
        Address updated = addressService.updateAddress(addressId, updateRequest);

        assertThat(updated).isNotNull();
        assertThat(updated.getStreet()).isEqualTo("Nova");
    }


    @Test
    void shouldReturnAddressesByUser() {
        UUID userId = UUID.randomUUID();
        when(addressRepository.findAllByUserId(userId)).thenReturn(List.of(new Address()));
        List<Address> addresses = addressService.getAddressesByUserId(userId);
        assertThat(addresses).hasSize(1);
    }

    @Test
    void shouldDeleteAddress() {
        UUID id = UUID.randomUUID();
        addressService.deleteAddress(id);
        verify(addressRepository).deleteById(id);
    }

    @Test
    void shouldReturnAddressById() {
        UUID id = UUID.randomUUID();
        Address address = new Address();
        address.setId(id);
        address.setStreet("Rua 1");

        when(addressRepository.findById(id)).thenReturn(Optional.of(address));

        Optional<Address> result = addressService.getAddressById(id);

        assertThat(result).isNotNull();
        assertThat(result.get().getId()).isEqualTo(id);
        assertThat(result.get().getStreet()).isEqualTo("Rua 1");
    }

    @Test
    void shouldThrowExceptionWhenAddressNotFound() {
        UUID id = UUID.randomUUID();
        when(addressRepository.findById(id)).thenReturn(Optional.empty());

        assertThrows(NoSuchElementException.class, () -> addressService.getAddressById(id));
    }
}

