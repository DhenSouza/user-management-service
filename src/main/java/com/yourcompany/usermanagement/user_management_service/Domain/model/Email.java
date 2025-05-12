package com.yourcompany.usermanagement.user_management_service.Domain.model;

import jakarta.validation.constraints.NotBlank;
import java.util.Objects;

public final class Email {

    @NotBlank
    private final String value;

    public Email(String value) {
        if (value == null || !value.matches("^[\\w-.]+@([\\w-]+\\.)+[\\w-]{2,4}$")) {
            throw new IllegalArgumentException("Invalid email format: " + value);
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true;
        if (!(obj instanceof Email other)) return false;
        return value.equalsIgnoreCase(other.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value.toLowerCase());
    }
}
