package com.yourcompany.usermanagement.user_management_service.application.service.user;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.Email;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.authorization.interfaces.IAuthorizationService;
import com.yourcompany.usermanagement.user_management_service.application.service.exceptions.UserNotFoundException;
import com.yourcompany.usermanagement.user_management_service.application.service.user.interfaces.IUserService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;


import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final IAuthorizationService authorizationService;

    public Page<User> listAllUsers(Pageable pageable) {
        authorizationService.ensureAdmin();
        return userRepository.findAll(pageable);
    }


    public Optional<User> getUserById(UUID id) {
        authorizationService.ensureSelfOrAdmin(id);
        return userRepository.findById(id);
    }

    public Optional<User> getEmailById(String email) {
        authorizationService.ensureSelfOrAdmin(email);
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(String name, String rawEmail, String rawPassword, Role role) {
        authorizationService.ensureSelfOrAdmin(rawEmail);
        Email email = new Email(rawEmail);

        if (userRepository.existsByEmail(email.getValue())) {
            throw new IllegalArgumentException("Email already in use.");
        }

        User user = User.builder()
                .name(name)
                .email(email.getValue())
                .password(passwordEncoder.encode(rawPassword))
                .role(role)
                .active(true)
                .build();

        return userRepository.save(user);
    }

    @Transactional
    public void deleteUser(UUID id) {
        authorizationService.ensureSelfOrAdmin(id);
        userRepository.deleteById(id);
    }

    @Transactional
    public User updatePassword(UUID userId, String newRawPassword) {
        authorizationService.ensureSelfOrAdmin(userId);
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newRawPassword));
        return userRepository.save(user);
    }

    @Transactional
    public User updateUser(UUID id, String name, String email, String rawPassword) {
        authorizationService.ensureSelfOrAdmin(id);
        User user = userRepository.findById(id)
                .orElseThrow(() -> new UserNotFoundException("User not found with ID: " + id));

        user.setName(name);
        user.setEmail(new Email(email).toString());
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        return userRepository.save(user);
    }
}