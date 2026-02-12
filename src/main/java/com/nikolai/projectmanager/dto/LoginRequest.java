package com.nikolai.projectmanager.dto;

public record LoginRequest(
        String email,
        String password
) {}