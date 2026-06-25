package com.ticketsystem.user.service;

import com.ticketsystem.user.dto.RegisterRequest;
import com.ticketsystem.user.dto.UserResponse;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.mapper.UserMapper;
import com.ticketsystem.user.repository.UserRepository;

public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    public UserServiceImpl(UserMapper userMapper, UserRepository userRepository) {
        this.userMapper = userMapper;
        this.userRepository = userRepository;
    }

    @Override
    public UserResponse register(RegisterRequest request) {
        // Implementation for user registration
        User user = userMapper.toEntity(request);
        // Save user to database
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public User getUserById(Long id) {
        // Implementation to retrieve user by ID
        return userRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("User not found"));
    }

}
