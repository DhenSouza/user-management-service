package com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import feign.Param;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface IUserRepository extends JpaRepository<User, UUID> {
    Optional<User> findByEmail(String email);
    boolean existsByEmail(String email);

    @Query("""
    SELECT u FROM User u
    WHERE LOWER(u.name) LIKE LOWER(CONCAT('%', :term, '%'))
       OR LOWER(u.email) LIKE LOWER(CONCAT('%', :term, '%'))
       OR LOWER(CAST(u.role AS string)) LIKE LOWER(CONCAT('%', :term, '%'))
""")
    Page<User> searchUsers(@Param("term") String term, Pageable pageable);
}
