package com.dims.marketplace.controller;


import com.dims.marketplace.dto.*;
import com.dims.marketplace.dto.auth.AuthResponse;
import com.dims.marketplace.dto.auth.LoginRequest;
import com.dims.marketplace.dto.auth.RegisterRequest;
import com.dims.marketplace.service.inter.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;

    @PostMapping("/register")
    public String register(@RequestBody @Valid RegisterRequest request) {
        authService.register(request);
        return "User registered successfully";
    }

    @PostMapping("/login")
    public AuthResponse login(@RequestBody @Valid LoginRequest request) {
        return authService.login(request);
    }
}