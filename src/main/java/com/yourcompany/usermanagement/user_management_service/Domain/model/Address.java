package com.yourcompany.usermanagement.user_management_service.Domain.model;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import lombok.*;

import java.util.UUID;

@Entity
@Table(name = "addresses")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Address {

    @Id
    @GeneratedValue
    private UUID id;

    @NotBlank
    @Column(nullable = false)
    private String street;

    @NotBlank
    @Column(nullable = false)
    private String number;

    private String complement;

    @NotBlank
    @Column(nullable = false)
    private String neighborhood;

    @NotBlank
    @Column(nullable = false)
    private String city;

    @NotBlank
    @Column(nullable = false, length = 2)
    private String state;

    @NotBlank
    @Column(name = "postal_code", nullable = false, length = 10)
    private String postalCode;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

}
