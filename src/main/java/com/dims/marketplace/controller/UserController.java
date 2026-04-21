package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.enums.Role;
import com.dims.marketplace.dto.mapper.UserMapper;
import com.dims.marketplace.dto.user.UserRequest;
import com.dims.marketplace.dto.user.UserResponse;
import com.dims.marketplace.dto.user.update.UserUpdateRequest;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.service.inter.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    @PostMapping
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request) {

        User user = userService.createUser(request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new ApiResponse<>(
                        HttpStatus.CREATED.value(),
                        "User created successfully",
                        UserMapper.toResponse(user)
                ));
    }

    // ✅ GET ALL + PAGINATION + FILTER ROLE
    @GetMapping
    public ResponseEntity<ApiResponse<Page<UserResponse>>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) Role role) {

        Pageable pageable = PageRequest.of(page, size);

        Page<User> users;

        if (role != null) {
            users = userService.getUsersByRole(role, pageable);
        } else {
            users = userService.getAllUsers(pageable);
        }

        Page<UserResponse> response = users.map(UserMapper::toResponse);

        return ResponseEntity.ok(new ApiResponse<>(200, "Success", response));
    }

    // ✅ GET BY ID
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getById(@PathVariable UUID id) {

        User user = userService.getUserById(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", UserMapper.toResponse(user))
        );
    }

    // ✅ GET BY EMAIL (SINGLE RESOURCE)
    @GetMapping("/by-email")
    public ResponseEntity<ApiResponse<UserResponse>> getByEmail(@RequestParam String email) {

        User user = userService.getUserByEmail(email);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "Success", UserMapper.toResponse(user))
        );
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser(
            @PathVariable UUID id,
            @Valid @RequestBody UserUpdateRequest request) {

        User user = userService.updateUser(id, request);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User updated successfully", UserMapper.toResponse(user))
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<Void>> deleteUser(@PathVariable UUID id) {

        userService.deleteUser(id);

        return ResponseEntity.ok(
                new ApiResponse<>(200, "User deleted successfully", null)
        );
    }
}