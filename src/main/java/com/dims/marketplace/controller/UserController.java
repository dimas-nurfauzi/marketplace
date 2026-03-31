package com.dims.marketplace.controller;

import com.dims.marketplace.dto.ApiResponse;
import com.dims.marketplace.dto.user.UserRequest;
import com.dims.marketplace.dto.user.UserResponse;
import com.dims.marketplace.dto.user.update.UserUpdateRequest;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.service.inter.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequiredArgsConstructor
@RequestMapping("/user")
public class UserController {
    private final UserService userService;

    @PostMapping("/create")
    public ResponseEntity<ApiResponse<UserResponse>> createUser(
            @Valid @RequestBody UserRequest request) {

        User user = userService.createUser(request);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(
                HttpStatus.CREATED.value(),
                "User created successfully",
                userResponse
        );

        return new ResponseEntity<>(response, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<UserResponse>>> getAllUsers() {

        List<User> users = userService.getAllUsers();

        List<UserResponse> responses = users.stream()
                .map(user -> new UserResponse(
                        user.getId(),
                        user.getFullName(),
                        user.getEmail(),
                        user.getCreatedAt()
                ))
                .toList();

        ApiResponse<List<UserResponse>> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                responses
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> getUserById(@PathVariable UUID id) {

        User user = userService.getUserById(id);

        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                userResponse
        );

        return ResponseEntity.ok(response);
    }

    @GetMapping("/email")
    public ResponseEntity<ApiResponse<UserResponse>> getUserByEmail(@RequestParam String email) {
        User user = userService.getUserByEmail(email);
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "Success",
                userResponse
        );
        return ResponseEntity.ok(response);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<ApiResponse<UserResponse>> updateUser (@PathVariable UUID id, @Valid @RequestBody UserUpdateRequest request) {
        User user = userService.updateUser(id, request);
        UserResponse userResponse = new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getCreatedAt()
        );

        ApiResponse<UserResponse> response = new ApiResponse<>(
                HttpStatus.OK.value(),
                "User has been update!",
                userResponse
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteUser(
            @PathVariable UUID id){

        userService.deleteUser(id);

        return ResponseEntity.noContent().build();
    }
}
