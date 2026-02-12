package com.nikolai.projectmanager.dto;

public record AuthResponse(
        String token,
        String email,
        String username
) {}