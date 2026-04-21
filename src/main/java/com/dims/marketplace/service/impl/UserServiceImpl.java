package com.dims.marketplace.service.impl;

import com.dims.marketplace.dto.enums.Role;
import com.dims.marketplace.dto.user.UserRequest;
import com.dims.marketplace.dto.user.update.UserUpdateRequest;
import com.dims.marketplace.entity.User;
import com.dims.marketplace.exceptions.DuplicateException;
import com.dims.marketplace.exceptions.NotFoundException;
import com.dims.marketplace.repository.UserRepository;
import com.dims.marketplace.service.inter.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@Transactional
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User createUser (UserRequest request) {
        if (userRepository.existsByEmail(request.getEmail())){
            throw new DuplicateException("Email already exists");
        }

        User user = new User();
        user.setFullName(request.getFullName());
        user.setEmail(request.getEmail());
        user.setRole(request.getRole());
        user.setPassword(request.getPassword());
        return userRepository.save(user);
    }

    @Override
    public Page<User> getAllUsers(Pageable pageable) {
        return userRepository.findAll(pageable);
    }

    @Override
    public Page<User> getUsersByRole(Role role, Pageable pageable) {
        return userRepository.findByRole(role, pageable);
    }
    @Override
    public User getUserById(UUID id) {
        return userRepository.findById(id).orElseThrow( () -> new NotFoundException("User not found"));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email).orElseThrow(()-> new NotFoundException("user with given email cannot be found."));
    }

    // need fix: field email bisa kosong ketika di update
    @Override
    public User updateUser (UUID id, UserUpdateRequest request) {
        User user = userRepository.findById(id)
                .orElseThrow( () -> new NotFoundException("User not found"));
        // supaya gak ngetag email sendiri sebagai duplicate
        if (request.getEmail() != null &&
                !request.getEmail().isBlank() &&
                !request.getEmail().equals(user.getEmail()) &&
                userRepository.existsByEmail(request.getEmail())) {

            throw new DuplicateException("Email is already registered");
        }

        if (request.getFullName() != null) {
            user.setFullName(request.getFullName());
        }

        if (request.getEmail() != null) {
            user.setEmail(request.getEmail());
        }

        if (request.getPassword() != null) {
            user.setPassword(request.getPassword());
        }

        return userRepository.save(user);
    }

    @Override
    public void deleteUser(UUID id) {

        User user = userRepository.findById(id)
                .orElseThrow(() -> new NotFoundException("User not found"));

        userRepository.delete(user);
    }
}