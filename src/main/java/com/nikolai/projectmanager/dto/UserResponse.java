package com.nikolai.projectmanager.dto;

public record UserResponse(
        Long id,
        String email,
        String username,
        String firstName,
        String lastName
) {}