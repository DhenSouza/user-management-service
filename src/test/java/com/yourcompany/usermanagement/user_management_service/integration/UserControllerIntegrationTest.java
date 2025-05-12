package com.yourcompany.usermanagement.user_management_service.integration;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.web.login.dto.PasswordUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.user.dto.UserCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.user.dto.UserUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import com.yourcompany.usermanagement.user_management_service.infrastructure.security.jwt.JwtUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
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

    @Autowired
    private JwtUtil jwtUtil;

    private User user;
    private String jwt;

    @BeforeEach
    void setup() {
        userRepository.deleteAll();

        user = userRepository.save(User.builder()
                .name("Admin")
                .email("admin@example.com")
                .password(new BCryptPasswordEncoder().encode("admin123"))
                .role(Role.ADMIN)
                .active(true)
                .build());

        jwt = jwtUtil.generateToken(user.getEmail());
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

        mockMvc.perform(get("/api/users/" + user.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.email").value("alice@example.com"));
    }

    @Test
    void shouldReturnBadRequestWhenCreatingInvalidUser() throws Exception {
        UserCreateRequest request = new UserCreateRequest("", "invalid-email", "123");

        mockMvc.perform(post("/api/users")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isBadRequest());
    }

    @Test
    void shouldListAllUsers() throws Exception {
        userRepository.save(User.builder()
                .name("A")
                .email("a@a.com")
                .password("123")
                .role(Role.USER)
                .active(true)
                .build());

        mockMvc.perform(get("/api/users")
                        .param("page", "0")
                        .param("size", "10")
                        .param("sort", "name,asc")
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isArray())
                .andExpect(jsonPath("$.content[?(@.name == 'A')]").exists());
    }



    @Test
    void shouldUpdatePasswordSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder().name("User").email("u@x.com").password("old").role(Role.USER).active(true).build());

        mockMvc.perform(put("/api/users/" + saved.getId() + "/password")
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(
                                new PasswordUpdateRequest("newPass123")
                        )))
                .andExpect(status().isOk());
    }

    @Test
    void shouldDeleteUserSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder().name("User").email("x@x.com").password("123").role(Role.USER).active(true).build());

        mockMvc.perform(delete("/api/users/" + saved.getId())
                        .header("Authorization", "Bearer " + jwt))
                .andExpect(status().isNoContent());
    }

    @Test
    void shouldUpdateUserSuccessfully() throws Exception {
        User saved = userRepository.save(User.builder()
                .name("Old")
                .email("old@e.com")
                .password("123")
                .role(Role.USER)
                .active(true)
                .build());

        UserUpdateRequest updateRequest = new UserUpdateRequest(
                "New Name",
                "new@email.com",
                "newSecurePassword123"
        );

        mockMvc.perform(put("/api/users/" + saved.getId())
                        .header("Authorization", "Bearer " + jwt)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updateRequest)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("New Name"))
                .andExpect(jsonPath("$.email").value("new@email.com"));
    }
}
