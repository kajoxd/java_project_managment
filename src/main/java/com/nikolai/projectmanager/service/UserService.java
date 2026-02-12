package com.nikolai.projectmanager.service;

import com.nikolai.projectmanager.dto.AuthResponse;
import com.nikolai.projectmanager.dto.LoginRequest;
import com.nikolai.projectmanager.dto.RegisterRequest;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    public AuthResponse register(RegisterRequest request) {
        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new RuntimeException("Email already exists");
        }

        User user = User.builder()
                .email(request.email())
                .username(request.username())
                .password(passwordEncoder.encode(request.password()))
                .firstName(request.firstName())
                .lastName(request.lastName())
                .build();

        userRepository.save(user);
        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getEmail(), user.getUsername());
    }

    public AuthResponse authenticate(LoginRequest request) {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.email(),
                        request.password()
                )
        );

        User user = userRepository.findByEmail(request.email())
                .orElseThrow(() -> new RuntimeException("User not found"));

        String jwtToken = jwtService.generateToken(user);

        return new AuthResponse(jwtToken, user.getEmail(), user.getUsername());
    }
}