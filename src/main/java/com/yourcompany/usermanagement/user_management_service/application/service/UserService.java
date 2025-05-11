package com.yourcompany.usermanagement.user_management_service.application.service;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.Email;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.service.interfaces.IUserService;
import com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces.IUserRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class UserService implements IUserService {

    private final IUserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    public List<User> listAllUsers() {
        return userRepository.findAll();
    }

    public Optional<User> getUserById(UUID id) {
        return userRepository.findById(id);
    }

    public Optional<User> getEmailById(String email) {
        return userRepository.findByEmail(email);
    }

    @Transactional
    public User createUser(String name, String rawEmail, String rawPassword, Role role) {
        Email email = new Email(rawEmail); // validação do VO

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
        userRepository.deleteById(id);
    }

    @Transactional
    public User updatePassword(UUID userId, String newRawPassword) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        user.setPassword(passwordEncoder.encode(newRawPassword));
        return userRepository.save(user);
    }
}
