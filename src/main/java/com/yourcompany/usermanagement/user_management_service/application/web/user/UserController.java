package com.yourcompany.usermanagement.user_management_service.application.web.user;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.user.interfaces.IUserService;
import com.yourcompany.usermanagement.user_management_service.application.web.login.dto.PasswordUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.mapper.UserMapper;
import com.yourcompany.usermanagement.user_management_service.application.web.user.dto.UserCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.user.dto.UserResponse;
import com.yourcompany.usermanagement.user_management_service.application.web.user.dto.UserUpdateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.NoSuchElementException;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<Page<UserResponse>> listUsers(Pageable pageable) {
        Page<UserResponse> users = userService.listAllUsers(pageable)
                .map(UserMapper::toResponse);
        return ResponseEntity.ok(users);
    }

    @GetMapping("/{id}")
    public ResponseEntity<UserResponse> getUser(@PathVariable UUID id) {
        User user = userService.getUserById(id)
                .orElseThrow(() -> new NoSuchElementException("User not found with ID: " + id));
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PostMapping
    public ResponseEntity<UserResponse> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(
                request.name(),
                request.email(),
                request.password(),
                Role.USER
        );
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<UserResponse> updatePassword(
            @PathVariable UUID id,
            @Valid @RequestBody PasswordUpdateRequest request) {
        User user = userService.updatePassword(id, request.password());
        return ResponseEntity.ok(UserMapper.toResponse(user));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}")
    public ResponseEntity<UserResponse> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {
        User updated = userService.updateUser(id, request.name(), request.email(), request.password());
        return ResponseEntity.ok(UserMapper.toResponse(updated));
    }
}
