package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.auth.AuthResponse;
import com.dims.marketplace.dto.auth.LoginRequest;
import com.dims.marketplace.dto.auth.RegisterRequest;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.exceptions.DuplicateException;
import com.dims.marketplace.repository.UserRepository;
import com.dims.marketplace.security.JwtService;
import com.dims.marketplace.service.inter.AuthService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {
    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    public void register(RegisterRequest request) {

        // 1. cek email sudah ada
        if (userRepository.existsByEmail(request.getEmail())) {
            throw new DuplicateException("Email already exists");
        }

        // 2. mapping request → entity
        User user = User.builder()
                .fullName(request.getFullName())
                .email(request.getEmail())
                .password(passwordEncoder.encode(request.getPassword()))
                .build();

        // role & timestamp otomatis dari @PrePersist

        // 3. save ke database
        userRepository.save(user);
    }

    @Override
    public AuthResponse login(LoginRequest request) {

        authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()
                )
        );

        String token = jwtService.generateToken(request.getEmail());

        return AuthResponse.builder()
                .token(token)
                .build();
    }
}
