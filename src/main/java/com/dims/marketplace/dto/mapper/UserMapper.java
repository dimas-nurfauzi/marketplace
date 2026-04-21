package com.dims.marketplace.dto.mapper;

import com.dims.marketplace.dto.user.UserResponse;
import com.dims.marketplace.entity.User;

public class UserMapper {

    public static UserResponse toResponse(User user) {
        return new UserResponse(
                user.getId(),
                user.getFullName(),
                user.getEmail(),
                user.getRole().name(),
                user.getCreatedAt()
        );
    }
}