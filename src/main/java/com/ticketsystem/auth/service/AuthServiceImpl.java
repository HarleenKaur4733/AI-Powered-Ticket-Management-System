package com.ticketsystem.auth.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.ticketsystem.auth.dto.LoginRequest;
import com.ticketsystem.auth.dto.LoginResponse;
import com.ticketsystem.security.CustomUserDetailsService;
import com.ticketsystem.security.JwtService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

    private final AuthenticationManager authenticationManager;
    private final CustomUserDetailsService customUserDetailsService;
    private final JwtService jwtService;

    @Override
    public LoginResponse login(LoginRequest request) {

        Authentication authentication = authenticationManager.authenticate(

                new UsernamePasswordAuthenticationToken(
                        request.getEmail(),
                        request.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();

        String token = jwtService.generateToken(userDetails);

        return LoginResponse.builder()
                .token(token)
                .build();
    }
}
