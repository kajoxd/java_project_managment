package com.nikolai.projectmanager.service;

import com.nikolai.projectmanager.dto.AuthResponse;
import com.nikolai.projectmanager.dto.LoginRequest;
import com.nikolai.projectmanager.dto.RegisterRequest;
import com.nikolai.projectmanager.dto.UserResponse;
import com.nikolai.projectmanager.exception.*;
import com.nikolai.projectmanager.model.User;
import com.nikolai.projectmanager.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.regex.Pattern;

@Service
@RequiredArgsConstructor
public class UserService {
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;
    private final AuthenticationManager authenticationManager;

    private static final Pattern EMAIL_PATTERN =
        Pattern.compile("^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$");


    public AuthResponse register(RegisterRequest request) {
        if (!isValidEmail(request.email())) {
            throw new InvalidEmailException("Email is not valid");
        }

        if (userRepository.findByEmail(request.email()).isPresent()) {
            throw new UserAlreadyExistsException("Email already exists");
        }

        if (userRepository.findByUsername(request.username()).isPresent()) {
            throw new UserAlreadyExistsException("Username already exists");
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
    try {
        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );
    } catch (AuthenticationException ex) {
        User user = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new UserNotFoundException("User not found"));

        throw new InvalidPasswordException("Invalid password");
    }

    User user = userRepository.findByUsername(request.username())
            .orElseThrow(() -> new UserNotFoundException("User not found"));

    String jwtToken = jwtService.generateToken(user);

    return new AuthResponse(jwtToken, user.getEmail(), user.getUsername());
}

    public List<UserResponse> searchUsersByUsername(String username) {
        List<User> users = userRepository.findByUsernameContainingIgnoreCase(username);
        return users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getEmail(),
                        user.getUsername(),
                        user.getFirstName(),
                        user.getLastName()
                ))
                .toList();
    }

    public UserResponse findUserByUsername(String username) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return new UserResponse(
                user.getId(),
                user.getEmail(),
                user.getUsername(),
                user.getFirstName(),
                user.getLastName()
        );
    }

    private boolean isValidEmail(String email) {
        return email != null && EMAIL_PATTERN.matcher(email).matches();
    }
}