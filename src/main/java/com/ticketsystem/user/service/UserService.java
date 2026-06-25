package com.ticketsystem.user.service;

import com.ticketsystem.user.dto.RegisterRequest;
import com.ticketsystem.user.dto.UserResponse;
import com.ticketsystem.user.entity.User;

public interface UserService {

    UserResponse register(RegisterRequest request);

    User getUserById(Long id);

}