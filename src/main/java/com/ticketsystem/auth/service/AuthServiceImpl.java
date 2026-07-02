package com.ticketsystem.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Service;

import com.ticketsystem.auth.dto.LoginRequest;
import com.ticketsystem.auth.dto.LoginResponse;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;

    @Override
    public LoginResponse login(LoginRequest request) {

        // Authentication Manager accepts a UsernamePasswordAuthenticationToken object
        // which contains the user's credentials (email and password) for
        // authentication.
        authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        return LoginResponse.builder()
                .token("JWT will come here")
                .build();
    }
}
