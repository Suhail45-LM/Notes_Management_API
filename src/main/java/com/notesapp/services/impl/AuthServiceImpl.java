package com.notesapp.services.impl;

import com.notesapp.dtos.request.LoginRequest;
import com.notesapp.dtos.request.RegisterRequest;
import com.notesapp.dtos.response.AuthResponse;
import com.notesapp.models.Role;
import com.notesapp.models.User;
import com.notesapp.exceptions.BadRequestException;
import com.notesapp.repositories.UserRepository;
import com.notesapp.security.JwtService;
import com.notesapp.services.AuthService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final UserRepository        userRepository;
    private final PasswordEncoder       passwordEncoder;
    private final JwtService            jwtService;
    private final AuthenticationManager authenticationManager;

    @Override
    @Transactional
    public AuthResponse register(RegisterRequest request) {

        if (userRepository.existsByUsername(request.username())) {
            throw new BadRequestException(
                    "Username '" + request.username() + "' is already taken");
        }

        if (userRepository.existsByEmail(request.email())) {
            throw new BadRequestException(
                    "Email '" + request.email() + "' is already registered");
        }
        User user = User.builder()
                .username(request.username())
                .email(request.email())
                .password(passwordEncoder.encode(request.password()))   // BCrypt hash
                .role(Role.USER)
                .build();

        User savedUser = userRepository.save(user);
        String token   = jwtService.generateToken(savedUser);

        log.info("New user registered: username='{}', role='{}'",
                savedUser.getUsername(), savedUser.getRole());

        return AuthResponse.of(
                savedUser.getId(),
                savedUser.getUsername(),
                savedUser.getEmail(),
                savedUser.getRole(),
                token
        );
    }

    @Override
    @Transactional(readOnly = true)
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.username(),
                        request.password()
                )
        );

        User user  = userRepository.findByUsername(request.username())
                .orElseThrow(() -> new BadRequestException("User not found"));

        String token = jwtService.generateToken(user);

        log.info("User logged in: username='{}'", user.getUsername());

        return AuthResponse.of(
                user.getId(),
                user.getUsername(),
                user.getEmail(),
                user.getRole(),
                token
        );
    }
}
