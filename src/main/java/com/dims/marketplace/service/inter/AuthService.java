package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.auth.AuthResponse;
import com.dims.marketplace.dto.auth.LoginRequest;
import com.dims.marketplace.dto.auth.RegisterRequest;

public interface AuthService {
    void register(RegisterRequest request);

    AuthResponse login(LoginRequest request);
}
