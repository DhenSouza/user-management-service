package com.yourcompany.usermanagement.user_management_service.unitTest;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.verify;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.exception.UserNotFoundException;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.authorization.interfaces.IAuthorizationService;
import com.yourcompany.usermanagement.user_management_service.application.service.user.UserService;

import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.security.crypto.password.PasswordEncoder;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {

    @Mock private IUserRepository userRepository;
    @Mock private PasswordEncoder passwordEncoder;
    @Mock private IAuthorizationService authorizationService;

    @InjectMocks private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        userId = UUID.randomUUID();
        user = User.builder()
                .id(userId)
                .name("John Doe")
                .email("john@example.com")
                .password("hashed_password")
                .role(Role.USER)
                .active(true)
                .build();
    }

    @Test
    void shouldUpdateExistingUser() {
        // autoriza
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);

        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode(anyString())).willReturn("ignored");
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User updated = userService.updateUser(userId, "Jane Doe", "jane@example.com", "123");

        assertThat(updated.getName()).isEqualTo("Jane Doe");
        assertThat(updated.getEmail()).isEqualTo("jane@example.com");
        verify(userRepository).save(updated);
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentUser() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.updateUser(userId, "Jane Doe", "jane@example.com", "newPass")
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining(userId.toString());
    }

    @Test
    void shouldListAllUsers() {
        willDoNothing().given(authorizationService).ensureAdmin();

        Pageable pageReq = PageRequest.of(0, 10);
        Page<User> mockPage = new PageImpl<>(List.of(user), pageReq, 1);
        given(userRepository.findAll(pageReq)).willReturn(mockPage);

        Page<User> result = userService.listAllUsers(pageReq);

        assertThat(result.getTotalElements()).isEqualTo(1);
        assertThat(result.getContent().get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldGetUserById() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(userId);

        assertThat(found).isPresent().contains(user);
    }

    @Test
    void shouldGetUserByEmail() {
        String email = user.getEmail();
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(email);
        given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

        Optional<User> found = userService.getEmailById(email);

        assertThat(found).isPresent().contains(user);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String rawPassword = "newPass123";
        String rawEmail    = user.getEmail();

        willDoNothing().given(authorizationService).ensureSelfOrAdmin(rawEmail);
        given(userRepository.existsByEmail(rawEmail)).willReturn(false);
        given(passwordEncoder.encode(rawPassword)).willReturn("encodedPassword");
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User result = userService.createUser(user.getName(), rawEmail, rawPassword, Role.USER);

        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(rawEmail);
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        String rawEmail = "exists@example.com";

        willDoNothing().given(authorizationService).ensureSelfOrAdmin(rawEmail);
        given(userRepository.existsByEmail(rawEmail)).willReturn(true);

        assertThatThrownBy(() ->
                userService.createUser("Name", rawEmail, "pass123", Role.USER)
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("Email already in use.");
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);
        given(userRepository.findById(userId)).willReturn(Optional.of(user));
        given(passwordEncoder.encode("newPass")).willReturn("encodedNewPass");
        given(userRepository.save(any(User.class))).willAnswer(inv -> inv.getArgument(0));

        User result = userService.updatePassword(userId, "newPass");

        assertThat(result.getPassword()).isEqualTo("encodedNewPass");
    }

    @Test
    void shouldThrowWhenUserNotFoundForPasswordUpdate() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        assertThatThrownBy(() ->
                userService.updatePassword(userId, "newPass")
        )
                .isInstanceOf(UserNotFoundException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteUser() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);

        userService.deleteUser(userId);

        verify(userRepository).deleteById(userId);
    }

    @Test
    void shouldReturnEmptyWhenGetByIdNotFound() {
        willDoNothing().given(authorizationService).ensureSelfOrAdmin(userId);
        given(userRepository.findById(userId)).willReturn(Optional.empty());

        Optional<User> result = userService.getUserById(userId);

        assertThat(result).isEmpty();
    }

}