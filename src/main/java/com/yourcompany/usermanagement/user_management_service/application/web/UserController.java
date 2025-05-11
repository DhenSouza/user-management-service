package com.yourcompany.usermanagement.user_management_service.application.web;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.UserService;
import com.yourcompany.usermanagement.user_management_service.application.service.interfaces.IUserService;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.PasswordUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.UserCreateRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/users")
@RequiredArgsConstructor
public class UserController {

    private final IUserService userService;

    @GetMapping
    public ResponseEntity<List<User>> listUsers() {
        return ResponseEntity.ok(userService.listAllUsers());
    }

    @GetMapping("/{id}")
    public ResponseEntity<User> getUser(@PathVariable UUID id) {
        return userService.getUserById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PostMapping
    public ResponseEntity<User> createUser(@Valid @RequestBody UserCreateRequest request) {
        User user = userService.createUser(
                request.name(),
                request.email(),
                request.password(),
                Role.USER
        );
        return ResponseEntity.ok(user);
    }

    @PutMapping("/{id}/password")
    public ResponseEntity<User> updatePassword(@PathVariable UUID id, @RequestBody PasswordUpdateRequest request) {
        return ResponseEntity.ok(userService.updatePassword(id, request.password()));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(@PathVariable UUID id) {
        userService.deleteUser(id);
        return ResponseEntity.noContent().build();
    }
}
