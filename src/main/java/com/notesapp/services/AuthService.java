package com.notesapp.services;

import com.notesapp.dtos.request.LoginRequest;
import com.notesapp.dtos.request.RegisterRequest;
import com.notesapp.dtos.response.AuthResponse;

public interface AuthService {

    AuthResponse register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
