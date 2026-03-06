package com.notesapp.dtos.response;

import com.notesapp.models.Role;
import lombok.Builder;

import java.util.UUID;


@Builder
public record AuthResponse(
        UUID   id,
        String username,
        String email,
        Role   role,
        String accessToken,
        String tokenType
) {
    public static AuthResponse of(UUID id, String username, String email,
                                  Role role, String accessToken) {
        return AuthResponse.builder()
                .id(id)
                .username(username)
                .email(email)
                .role(role)
                .accessToken(accessToken)
                .tokenType("Bearer")
                .build();
    }
}
