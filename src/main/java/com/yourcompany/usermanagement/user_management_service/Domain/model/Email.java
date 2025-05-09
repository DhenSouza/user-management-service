package com.yourcompany.usermanagement.user_management_service.Domain.model;

import jakarta.validation.constraints.NotBlank;

import java.util.Objects;

public class Email {

    @NotBlank
    @jakarta.validation.constraints.Email
    private final String value;

    public Email(String value) {
        if (value == null || !value.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Email)) return false;
        Email email = (Email) o;
        return value.equalsIgnoreCase(email.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }

    @Override
    public String toString() {
        return value;
    }
}
