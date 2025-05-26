package com.yourcompany.usermanagement.user_management_service.unitTest;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.address.AddressService;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.errorHandler.ExternalServiceException;
import com.yourcompany.usermanagement.user_management_service.application.web.errorHandler.ResourceNotFoundException;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.CepService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.mock.mockito.MockBean;

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
    @Mock private CepService cepService;      // <— corrigido de @MockBean para @Mock

    @InjectMocks private AddressService addressService;

    @BeforeEach
    void setup() {
        MockitoAnnotations.openMocks(this);
    }

    /**
     * Helper que devolve um ViaCepResponse configurado para o CEP passado.
     */
    private ViaCepResponse makeViaCep(String cep) {
        ViaCepResponse v = new ViaCepResponse();
        v.setPostalCode(cep);
        v.setStreet("Rua A");
        v.setComplement("");
        v.setNeighborhood("Centro");
        v.setCity("Cidade");
        v.setState("SP");
        // campos extras…
        return v;
    }

    @Test
    void shouldCreateAddressSuccessfully() {
        // --- prepara mocks ---
        UUID userId = UUID.randomUUID();
        User user = new User();
        user.setId(userId);
        user.setRole(Role.USER);
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        // stub do CepService *antes* de chamar createAddress()
        String cep = "12345-000";
        when(cepService.getCodePostal(cep))
                .thenReturn(makeViaCep(cep));

        // stub do repositório para retornar o próprio objeto salvo
        when(addressRepository.save(any(Address.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        // --- execução ---
        AddressCreateRequest request =
                new AddressCreateRequest(
                        "Rua A", "123", "Apto 1",
                        "Centro", "Cidade", "SP", cep
                );
        Address result = addressService.createAddress(userId, request);

        // --- verificações ---
        assertThat(result).isNotNull();
        assertThat(result.getStreet()).isEqualTo("Rua A");
        assertThat(result.getPostalCode()).isEqualTo(cep);
        assertThat(result.getUser()).isEqualTo(user);
        verify(addressRepository).save(any(Address.class));
    }

    @Test
    void shouldThrowWhenUserNotFound() {
        UUID userId = UUID.randomUUID();
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        AddressCreateRequest req = new AddressCreateRequest(
                "X","1","","B","C","ST","00000-000"
        );
        assertThrows(IllegalArgumentException.class,
                () -> addressService.createAddress(userId, req));
    }

    @Test
    void shouldUpdateAddressSuccessfully() {
        UUID addressId = UUID.randomUUID();
        Address existing = new Address();
        existing.setId(addressId);
        when(addressRepository.findById(addressId))
                .thenReturn(Optional.of(existing));

        String newCep = "03977-460";
        when(cepService.getCodePostal(newCep))
                .thenReturn(makeViaCep(newCep));

        when(addressRepository.save(any(Address.class)))
                .thenAnswer(inv -> inv.getArgument(0));

        AddressUpdateRequest update = new AddressUpdateRequest(
                "Nova","1","","Centro","Cidade","SP", newCep
        );
        Address updated = addressService.updateAddress(addressId, update);

        assertThat(updated).isNotNull();
        assertThat(updated.getStreet()).isEqualTo("Nova");
        assertThat(updated.getPostalCode()).isEqualTo(newCep);
    }

    @Test
    void shouldThrowWhenUpdateCepInvalid() {
        UUID addressId = UUID.randomUUID();
        Address existing = new Address();
        existing.setId(addressId);
        when(addressRepository.findById(addressId))
                .thenReturn(Optional.of(existing));

        // simula CEP não encontrado
        when(cepService.getCodePostal("99999-999"))
                .thenReturn(null);

        AddressUpdateRequest update = new AddressUpdateRequest(
                "X","1","","Y","Z","ST","99999-999"
        );
        assertThrows(
                ExternalServiceException.class,
                () -> addressService.updateAddress(addressId, update)
        );
    }

    @Test
    void shouldReturnAddressesByUser() {
        UUID userId = UUID.randomUUID();
        when(addressRepository.findAllByUserId(userId))
                .thenReturn(List.of(new Address()));
        var list = addressService.getAddressesByUserId(userId);
        assertThat(list).hasSize(1);
    }

    @Test
    void shouldDeleteAddress() {
        UUID id = UUID.randomUUID();
        when(addressRepository.existsById(id)).thenReturn(true);
        addressService.deleteAddress(id);
        verify(addressRepository).deleteById(id);
    }

    @Test
    void shouldThrowWhenDeleteNotFound() {
        UUID id = UUID.randomUUID();
        when(addressRepository.existsById(id)).thenReturn(false);
        assertThrows(
                ResourceNotFoundException.class,
                () -> addressService.deleteAddress(id)
        );
    }

    @Test
    void shouldReturnAddressById() {
        UUID id = UUID.randomUUID();
        Address addr = new Address();
        addr.setId(id);
        addr.setStreet("Rua 1");
        when(addressRepository.findById(id)).thenReturn(Optional.of(addr));

        var opt = addressService.getAddressById(id);
        assertThat(opt).isPresent();
        assertThat(opt.get().getId()).isEqualTo(id);
        assertThat(opt.get().getStreet()).isEqualTo("Rua 1");
    }

    @Test
    void shouldThrowWhenGetByIdNotFound() {
        UUID id = UUID.randomUUID();
        when(addressRepository.findById(id)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class,
                () -> addressService.getAddressById(id));
    }
}

