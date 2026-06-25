package com.ticketsystem.user.mapper;

import org.springframework.stereotype.Component;

import com.ticketsystem.user.dto.RegisterRequest;
import com.ticketsystem.user.dto.UserResponse;
import com.ticketsystem.user.entity.User;

@Component
public class UserMapper {

    public User toEntity(RegisterRequest request) {

        if (request == null) {
            return null;
        }

        return User.builder()
                .name(request.getName())
                .email(request.getEmail())
                .password(request.getPassword())
                .build();
    }

    public UserResponse toResponse(User user) {

        if (user == null) {
            return null;
        }

        return UserResponse.builder()
                .id(user.getId())
                .name(user.getName())
                .email(user.getEmail())
                .role(user.getRole())
                .build();
    }
}