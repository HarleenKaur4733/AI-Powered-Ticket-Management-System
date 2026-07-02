package com.ticketsystem.auth.service;

import com.ticketsystem.auth.dto.LoginRequest;
import com.ticketsystem.auth.dto.LoginResponse;

public interface AuthService {

    LoginResponse login(LoginRequest request);

}