package com.yourcompany.usermanagement.user_management_service.unitTest;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.UserService;
import com.yourcompany.usermanagement.user_management_service.application.service.exceptions.UserNotFoundException;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

public class UserServiceTest {

    @Mock
    private IUserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserService userService;

    private User user;
    private UUID userId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User updated = userService.updateUser(userId, "Jane Doe", "jane@example.com");

        assertThat(updated.getName()).isEqualTo("Jane Doe");
        assertThat(updated.getEmail()).isEqualTo("jane@example.com");
    }

    @Test
    void shouldThrowWhenUpdatingNonexistentUser() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updateUser(userId, "Jane Doe", "jane@example.com"))
                .isInstanceOf(UserNotFoundException.class)
                .hasMessageContaining("User not found with ID");
    }

    @Test
    void shouldListAllUsers() {
        List<User> mockUsers = List.of(user);
        when(userRepository.findAll()).thenReturn(mockUsers);

        List<User> result = userService.listAllUsers();

        assertThat(result).hasSize(1);
        assertThat(result.get(0).getEmail()).isEqualTo(user.getEmail());
    }

    @Test
    void shouldGetUserById() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> found = userService.getUserById(userId);

        assertThat(found).isPresent().contains(user);
    }

    @Test
    void shouldGetUserByEmail() {
        when(userRepository.findByEmail(user.getEmail())).thenReturn(Optional.of(user));

        Optional<User> found = userService.getEmailById(user.getEmail());

        assertThat(found).isPresent().contains(user);
    }

    @Test
    void shouldCreateUserSuccessfully() {
        String rawPassword = "newPass123";
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(rawPassword)).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.createUser(user.getName(), user.getEmail(), rawPassword, Role.USER);

        assertThat(result.getName()).isEqualTo(user.getName());
        assertThat(result.getEmail()).isEqualTo(user.getEmail());
        assertThat(result.getPassword()).isEqualTo("encodedPassword");
    }

    @Test
    void shouldThrowWhenEmailAlreadyExists() {
        when(userRepository.existsByEmail(anyString())).thenReturn(true);

        assertThatThrownBy(() -> userService.createUser("Name", "exists@email.com", "pass123", Role.USER))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("Email already in use.");
    }

    @Test
    void shouldUpdatePasswordSuccessfully() {
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));
        when(passwordEncoder.encode("newPass")).thenReturn("encodedNewPass");
        when(userRepository.save(any(User.class))).thenAnswer(i -> i.getArgument(0));

        User result = userService.updatePassword(userId, "newPass");

        assertThat(result.getPassword()).isEqualTo("encodedNewPass");
    }

    @Test
    void shouldThrowWhenUserNotFoundForPasswordUpdate() {
        when(userRepository.findById(userId)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> userService.updatePassword(userId, "pass"))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessage("User not found");
    }

    @Test
    void shouldDeleteUser() {
        userService.deleteUser(userId);
        verify(userRepository).deleteById(userId);
    }

}
