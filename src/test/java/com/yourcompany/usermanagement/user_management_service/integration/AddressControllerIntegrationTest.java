package com.yourcompany.usermanagement.user_management_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.address.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.CepService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.externalServices.viaCepApi.dto.ViaCepResponse;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IAddressRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;

import java.util.UUID;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class AddressControllerIntegrationTest {

    @Autowired private MockMvc mockMvc;
    @Autowired private IUserRepository userRepository;
    @Autowired private IAddressRepository addressRepository;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private JwtUtil jwtTokenProvider;

    private User user;
    private String jwtToken;

    @MockBean
    private CepService cepService;

    @BeforeEach
    void setup() {
        // limpa os dados
        addressRepository.deleteAll();
        userRepository.deleteAll();

        // cria um usuário de teste
        user = userRepository.save(User.builder()
                .name("Test User")
                .email("user@example.com")
                .password("123")             // idealmente já encode essa senha
                .role(Role.USER)
                .active(true)
                .build());

        // gera um JWT válido para esse usuário
        jwtToken = jwtTokenProvider.generateToken(
                user.getEmail()
        );
    }

    /**
     * Cria um ViaCepResponse preenchido via setters.
     */
    private ViaCepResponse makeViaCep() {
        ViaCepResponse v = new ViaCepResponse();
        v.setPostalCode("03977-460");
        v.setStreet("Rua A");
        v.setComplement("");
        v.setNeighborhood("Centro");
        v.setCity("Cidade");
        v.setState("SP");
        v.setIbgeCode("3550308");
        v.setGiaCode(null);
        v.setDdd("11");
        v.setSiafiCode("7107");
        return v;
    }

    /**
     * Chama o endpoint de criação e desserializa o Address criado.
     */
    private Address seedAddress() throws Exception {
        // stub do serviço de CEP
        when(cepService.getCodePostal("03977-460"))
                .thenReturn(makeViaCep());

        AddressCreateRequest req = new AddressCreateRequest(
                "Rua A", "12", "", "Centro", "Cidade", "SP", "03977-460"
        );

        MvcResult mvc = mockMvc.perform(post("/api/addresses/user/" + user.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(req)))
                .andExpect(status().isOk())
                .andReturn();

        String json = mvc.getResponse().getContentAsString();
        return objectMapper.readValue(json, Address.class);
    }

    @Test
    void shouldCreateAddress_withValidJwt() throws Exception {
        when(cepService.getCodePostal("03977-460"))
                .thenReturn(makeViaCep());

        AddressCreateRequest request =
                new AddressCreateRequest(
                        "Rua A",
                        "12",
                        "",
                        "Centro",
                        "Cidade",
                        "SP",
                        "03977-460"
                );

        mockMvc.perform(post("/api/addresses/user/" + user.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.street").value("Rua A"))
                .andExpect(jsonPath("$.userId").value(user.getId().toString()));
    }

    @Test
    void shouldDenyCreateAddress_withoutJwt() throws Exception {
        AddressCreateRequest request =
                new AddressCreateRequest(
                        "Rua A", "12", "", "Centro", "Cidade", "SP", "03977-460"
                );

        mockMvc.perform(post("/api/addresses/user/" + UUID.randomUUID())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isForbidden());
    }

    //=======================================================================
    //  DELETE
    //=======================================================================

    @Test
    void shouldDeleteAddress_success() throws Exception {
        // prepara um endereço no banco
        Address saved = seedAddress();

        // executa DELETE e espera 204 No Content
        mockMvc.perform(delete("/api/addresses/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNoContent());

        // ao buscar de novo, deve retornar 404 Not Found
        mockMvc.perform(get("/api/addresses/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnNotFoundOnDelete_nonexistent() throws Exception {
        mockMvc.perform(delete("/api/addresses/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }

    //=======================================================================
    //  UPDATE
    //=======================================================================

    @Test
    void shouldUpdateAddress_success() throws Exception {
        Address saved = seedAddress();

        ViaCepResponse viaCep2 = new ViaCepResponse();
        viaCep2.setPostalCode("01001-000");
        viaCep2.setStreet("Praça da Sé");
        viaCep2.setNeighborhood("Sé");
        viaCep2.setCity("São Paulo");
        viaCep2.setState("SP");
        when(cepService.getCodePostal("01001-000")).thenReturn(viaCep2);

        AddressUpdateRequest update = new AddressUpdateRequest(
                "Praça da Sé","100","","Sé","São Paulo","SP","01001-000"
        );

        mockMvc.perform(put("/api/addresses/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.street").value("Praça da Sé"))
                .andExpect(jsonPath("$.postalCode").value("01001-000"));
    }

    @Test
    void shouldReturnNotFoundOnUpdate_nonexistent() throws Exception {
        AddressUpdateRequest update = new AddressUpdateRequest(
                "X","1","","Y","Z","ZZ","00000-000"
        );

        mockMvc.perform(put("/api/addresses/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isNotFound());
    }

    @Test
    void shouldReturnBadRequestOnUpdate_invalidCep() throws Exception {
        Address saved = seedAddress();

        ViaCepResponse bad = new ViaCepResponse();
        bad.setPostalCode(null);
        when(cepService.getCodePostal("99999-999")).thenReturn(bad);

        AddressUpdateRequest update = new AddressUpdateRequest(
                "Rua X","99","","Bairro","Cidade","ST","0377-460"
        );

        mockMvc.perform(put("/api/addresses/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(update)))
                .andExpect(status().isBadRequest());
    }

    //=======================================================================
    //  GET BY ID
    //=======================================================================

    @Test
    void shouldGetAddressById_success() throws Exception {
        Address saved = seedAddress();

        mockMvc.perform(get("/api/addresses/" + saved.getId())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(saved.getId().toString()))
                .andExpect(jsonPath("$.street").value("Rua A"));
    }

    @Test
    void shouldReturnNotFoundOnGetById_nonexistent() throws Exception {
        mockMvc.perform(get("/api/addresses/" + UUID.randomUUID())
                        .header("Authorization", "Bearer " + jwtToken))
                .andExpect(status().isNotFound());
    }
}
