package com.nikolai.projectmanager.controller;

import com.nikolai.projectmanager.dto.RegisterRequest;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    @PostMapping("/register")
    public User register(@RequestBody RegisterRequest request) {
        return userService.register(request.email(), request.password());
    }
}
