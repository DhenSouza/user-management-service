package com.yourcompany.usermanagement.user_management_service.application.service.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface IUserService {
    List<User> listAllUsers();

    Optional<User> getUserById(UUID id);

    Optional<User> getEmailById(String email);

    User createUser(String name, String rawEmail, String rawPassword, Role role);

    public void deleteUser(UUID id);

    User updatePassword(UUID userId, String newRawPassword);

    User updateUser(UUID id, String name, String email);
}
