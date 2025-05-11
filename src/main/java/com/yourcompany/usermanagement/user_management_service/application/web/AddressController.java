package com.yourcompany.usermanagement.user_management_service.application.web;

import com.yourcompany.usermanagement.user_management_service.Domain.model.Address;
import com.yourcompany.usermanagement.user_management_service.application.service.interfaces.IAddressService;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.AddressCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.AddressResponse;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.AddressUpdateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.mapper.AddressMapper;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@Tag(name = "Addresses", description = "Address management endpoints")
@RestController
@RequestMapping("/api/addresses")
@RequiredArgsConstructor
public class AddressController {

    private final IAddressService addressService;

    @Operation(summary = "List addresses by user ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Addresses found"),
            @ApiResponse(responseCode = "404", description = "User not found")
    })
    @GetMapping("/user/{userId}")
    public ResponseEntity<List<AddressResponse>> listAddressesByUser(@PathVariable UUID userId) {
        return ResponseEntity.ok(
                addressService.getAddressesByUserId(userId)
                        .stream()
                        .map(AddressMapper::toResponse)
                        .toList()
        );
    }

    @Operation(summary = "Get an address by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address found"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @GetMapping("/{id}")
    public ResponseEntity<AddressResponse> getAddressById(@PathVariable UUID id) {
        Address address = addressService.getAddressById(id)
                .orElseThrow(() -> new IllegalArgumentException("Address not found with ID: " + id));
        return ResponseEntity.ok(AddressMapper.toResponse(address));
    }

    @Operation(summary = "Create an address for a user")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address created"),
            @ApiResponse(responseCode = "404", description = "User not found"),
            @ApiResponse(responseCode = "400", description = "Invalid address data")
    })
    @PostMapping("/user/{userId}")
    public ResponseEntity<AddressResponse> createAddress(
            @PathVariable UUID userId,
            @Valid @RequestBody AddressCreateRequest request) {

        Address address = addressService.createAddress(userId, request);
        return ResponseEntity.ok(AddressMapper.toResponse(address));
    }

    @Operation(summary = "Update an address")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Address updated"),
            @ApiResponse(responseCode = "404", description = "Address not found"),
            @ApiResponse(responseCode = "400", description = "Invalid address data")
    })
    @PutMapping("/{id}")
    public ResponseEntity<AddressResponse> updateAddress(
            @PathVariable UUID id,
            @Valid @RequestBody AddressUpdateRequest request) {

        Address address = addressService.updateAddress(id, request);
        return ResponseEntity.ok(AddressMapper.toResponse(address));
    }

    @Operation(summary = "Delete an address by ID")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "204", description = "Address deleted"),
            @ApiResponse(responseCode = "404", description = "Address not found")
    })
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteAddress(@PathVariable UUID id) {
        addressService.deleteAddress(id);
        return ResponseEntity.noContent().build();
    }
}