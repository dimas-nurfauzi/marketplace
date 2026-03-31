package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.user.UserRequest;
import com.dims.marketplace.dto.user.update.UserUpdateRequest;
import com.dims.marketplace.entity.User;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser (UserRequest request);

    List<User> getAllUsers();

    User getUserById(UUID id);

    User getUserByEmail(String email);

    User updateUser(UUID id, UserUpdateRequest request);

    void deleteUser(UUID id);
}
