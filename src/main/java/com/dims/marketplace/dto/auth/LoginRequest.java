package com.dims.marketplace.dto.auth;

import lombok.Data;

@Data
public class LoginRequest {
    private String email;
    private String password;
}