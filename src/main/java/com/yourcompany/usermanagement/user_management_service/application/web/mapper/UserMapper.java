package com.yourcompany.usermanagement.user_management_service.application.web.mapper;

import com.yourcompany.usermanagement.user_management_service.Domain.enums.Role;
import com.yourcompany.usermanagement.user_management_service.Domain.model.User;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.UserCreateRequest;
import com.yourcompany.usermanagement.user_management_service.application.web.dto.UserResponse;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getName(),
                user.getEmail(),
                user.getRole(),
                user.isActive()
        );
    }

    public static User toEntity(UserCreateRequest request, String hashedPassword) {
        return User.builder()
                .name(request.name())
                .email(request.email())
                .password(hashedPassword)
                .role(Role.USER) // ou request.role() se quiser parametrizar
                .active(true)
                .build();
    }
}
