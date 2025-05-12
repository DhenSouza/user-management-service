package com.yourcompany.usermanagement.user_management_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private IUserRepository userRepository;
    @Autowired private IAddressRepository addressRepository;
    @Autowired private ObjectMapper objectMapper;

    private User user;

    @BeforeEach
    void setup() {
        addressRepository.deleteAll();
        userRepository.deleteAll();
        user = userRepository.save(User.builder()
                .name("Test User").email("user@example.com").password("123")
                .role(Role.USER).active(true).build());
    }

    @Test
    void shouldCreateAddress() throws Exception {
        AddressCreateRequest request = new AddressCreateRequest("Rua A", "12", "", "Centro", "Cidade", "SP", "12345-678");

        mockMvc.perform(post("/api/addresses/user/" + user.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Rua A"));
    }

    @Test
    void shouldUpdateAddress() throws Exception {
        Address address = addressRepository.save(Address.builder()
                .street("Antiga").number("1").complement("")
                .neighborhood("Antigo Bairro").city("C").state("SP")
                .postalCode("00000-000").user(user).build());

        AddressUpdateRequest request = new AddressUpdateRequest("Nova", "99", "", "Bairro", "Nova C", "RJ", "12345-000");

        mockMvc.perform(put("/api/addresses/" + address.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Nova"));
    }

    @Test
    void shouldListAddressesByUserId() throws Exception {
        addressRepository.save(Address.builder()
                .street("R1").number("1").complement("Ap 1")
                .neighborhood("Centro").city("C").state("SP")
                .postalCode("00000-000").user(user).build());

        mockMvc.perform(get("/api/addresses/user/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].street").value("R1"));
    }

    @Test
    void shouldDeleteAddress() throws Exception {
        Address address = addressRepository.save(Address.builder()
                .street("R2").number("2").complement("")
                .neighborhood("Centro").city("C2").state("SP")
                .postalCode("00000-000").user(user).build());

        mockMvc.perform(delete("/api/addresses/" + address.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldReturnAddressById() throws Exception {
        Address saved = addressRepository.save(Address.builder()
                .street("Rua Teste").number("10").neighborhood("Centro")
                .city("Cidade").state("SP").postalCode("12345-000").user(user).build());

        mockMvc.perform(get("/api/addresses/" + saved.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Rua Teste"));
    }

    @Test
    void shouldReturnNotFoundWhenAddressDoesNotExist() throws Exception {
        UUID fakeId = UUID.randomUUID();

        mockMvc.perform(get("/api/addresses/" + fakeId))
                .andExpect(status().isNotFound());
    }

}
