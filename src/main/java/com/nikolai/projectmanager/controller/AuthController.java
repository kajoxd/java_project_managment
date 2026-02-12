
package com.nikolai.projectmanager.controller;

import com.nikolai.projectmanager.dto.AuthResponse;
import com.nikolai.projectmanager.dto.LoginRequest;
import com.nikolai.projectmanager.dto.RegisterRequest;
import com.nikolai.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest request) {
        return ResponseEntity.ok(userService.register(request));
    }

    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest request) {
        return ResponseEntity.ok(userService.authenticate(request));
    }
}