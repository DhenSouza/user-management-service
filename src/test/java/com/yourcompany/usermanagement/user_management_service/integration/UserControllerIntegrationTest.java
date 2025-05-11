package com.yourcompany.usermanagement.user_management_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.PasswordUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.UserCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.UserUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@SpringBootTest
@AutoConfigureMockMvc
public class UserControllerIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private IUserRepository userRepository;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();
    }

    @Test
    void shouldCreateUserAndFetchById() throws Exception {
        User user = userRepository.save(User.builder()
                .name("Alice")
                .email("alice@example.com")
                .password("12345678")
                .role(Role.USER)
                .active(true)
                .build());

        mockMvc.perform(get("/api/users/" + user.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest("", "invalid-email", "123");

        mockMvc.perform(post("/api/users")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListAllUsers() throws Exception {
        userRepository.save(User.builder().name("A").email("a@a.com").password("123").role(Role.USER).active(true).build());

        mockMvc.perform(get("/api/users"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$[0].name").value("A"));
    }

    @Test
    void shouldUpdatePasswordSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder().name("User").email("u@x.com").password("old").role(Role.USER).active(true).build());

        mockMvc.perform(put("/api/users/" + saved.getId() + "/password")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PasswordUpdateRequest("newPass123")
                        )))
                .andExpect(status().isOk());

    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder().name("User").email("x@x.com").password("123").role(Role.USER).active(true).build());

        mockMvc.perform(delete("/api/users/" + saved.getId()))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder().name("Old").email("old@e.com").password("123").role(Role.USER).active(true).build());

        mockMvc.perform(put("/api/users/" + saved.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(new UserUpdateRequest("New Name", "new@email.com"))))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"));
    }

}
