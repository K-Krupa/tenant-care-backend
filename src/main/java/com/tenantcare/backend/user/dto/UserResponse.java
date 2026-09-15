package com.tenantcare.backend.user.dto;

import com.tenantcare.backend.user.Role;

public record UserResponse(
        Long id,
        String email,
        String firstName,
        String lastName,
        Role role
) {
}
