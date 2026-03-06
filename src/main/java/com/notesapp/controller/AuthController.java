package com.notesapp.controller;

import com.notesapp.dtos.request.LoginRequest;
import com.notesapp.dtos.request.RegisterRequest;
import com.notesapp.dtos.response.ApiResponse;
import com.notesapp.dtos.response.AuthResponse;
import com.notesapp.services.AuthService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
@Tag(name = "Authentication", description = "Register and login endpoints")
public class AuthController {

    private final AuthService authService;



    @PostMapping("/register")
    @Operation(summary = "Register a new user",
               description = "Creates a new account with role USER and returns a JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> register(
            @Valid @RequestBody RegisterRequest request) {

        AuthResponse authResponse = authService.register(request);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.created("User registered successfully", authResponse));
    }



    @PostMapping("/login")
    @Operation(summary = "Login",
               description = "Authenticates user credentials and returns a JWT token")
    public ResponseEntity<ApiResponse<AuthResponse>> login(
            @Valid @RequestBody LoginRequest request) {

        AuthResponse authResponse = authService.login(request);
        return ResponseEntity.ok(ApiResponse.success("Login successful", authResponse));
    }
}
