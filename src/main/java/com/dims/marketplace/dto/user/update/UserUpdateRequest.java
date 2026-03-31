package com.dims.marketplace.dto.user.update;

import jakarta.validation.constraints.Email;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserUpdateRequest {
    private String fullName;
    @Email(message = "Enter a valid email address!")
    private String email;
    private String password;
}
