package com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);
}
