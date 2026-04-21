package com.dims.marketplace.service.inter;

import com.dims.marketplace.dto.enums.Role;
import com.dims.marketplace.dto.user.UserRequest;
import com.dims.marketplace.dto.user.update.UserUpdateRequest;
import com.dims.marketplace.entity.User;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.UUID;

public interface UserService {
    User createUser (UserRequest request);

    Page<User> getAllUsers(Pageable pageable);

    Page<User> getUsersByRole(Role role, Pageable pageable);

    User getUserById(UUID id);

    User getUserByEmail(String email);

    User updateUser(UUID id, UserUpdateRequest request);

    void deleteUser(UUID id);
}
