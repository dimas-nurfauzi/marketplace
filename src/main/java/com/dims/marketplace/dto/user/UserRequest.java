package com.dims.marketplace.dto.user;

import com.dims.marketplace.dto.enums.Role;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserRequest {
    @NotBlank(message = "This field cannot be empty!")
    private String fullName;
    @NotBlank(message = "Email cannot be empty!")
    @Email(message = "Enter a valid email address!")
    private String email;
    @Enumerated(EnumType.STRING)
    private Role role;
    @Pattern(
            regexp = "^(?=.*[A-Za-z])(?=.*\\d)[A-Za-z\\d]{6,}$",
            message = "Password must be at least 6 characters and contain both letters and numbers"
    )
    @NotBlank(message = "password is required")
    private String password;
}
