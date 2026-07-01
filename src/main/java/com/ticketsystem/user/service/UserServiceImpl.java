package com.ticketsystem.user.service;

import org.springframework.stereotype.Service;

import com.ticketsystem.exception.ResourceNotFoundException;
import com.ticketsystem.user.dto.RegisterRequest;
import com.ticketsystem.user.dto.UserResponse;
import com.ticketsystem.user.entity.Role;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.mapper.UserMapper;
import com.ticketsystem.user.repository.UserRepository;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserMapper userMapper;
    private final UserRepository userRepository;

    @Override
    public UserResponse register(RegisterRequest request) {
        // Implementation for user registration
        User user = userMapper.toEntity(request);
        user.setRole(Role.USER); // Set default role to USER);
        // Save user to database
        User savedUser = userRepository.save(user);
        return userMapper.toResponse(savedUser);
    }

    @Override
    public User getUserById(Long id) {
        // Implementation to retrieve user by ID
        return userRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("User not found"));
    }

}
