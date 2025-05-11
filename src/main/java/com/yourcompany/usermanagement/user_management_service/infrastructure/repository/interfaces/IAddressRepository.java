package com.yourcompany.usermanagement.user_management_service.infrastructure.repository.interfaces;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface IAddressRepository extends JpaRepository<Address, UUID> {
    List<Address> findAllByUserId(UUID userId);
}
