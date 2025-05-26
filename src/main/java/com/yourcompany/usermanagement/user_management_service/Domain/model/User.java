package com.yourcompany.usermanagement.user_management_service.Domain.model;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.infrastructure.utils.converter.RoleConverter;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank(message = "{NotBlank.user.name}")
    @Column(nullable = false)
    private String name;

    @NotBlank(message = "{NotBlank.user.email}")
    @jakarta.validation.constraints.Email
    @Column(nullable = false, unique = true)
    private String email;

    @NotBlank(message = "{NotBlank.user.password}")
    @Column(nullable = false)
    private String password;

    @Convert(converter = RoleConverter.class)
    @Column(nullable = false, columnDefinition = "role")
    private Role role = Role.USER;

    private boolean active;
}
