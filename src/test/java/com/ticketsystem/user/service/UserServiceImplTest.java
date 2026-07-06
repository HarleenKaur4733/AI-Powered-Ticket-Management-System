package com.ticketsystem.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.ticketsystem.exception.ResourceNotFoundException;
import com.ticketsystem.user.dto.RegisterRequest;
import com.ticketsystem.user.dto.UserResponse;
import com.ticketsystem.user.entity.Role;
import com.ticketsystem.user.entity.User;
import com.ticketsystem.user.mapper.UserMapper;
import com.ticketsystem.user.repository.UserRepository;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {

    @Mock
    private UserMapper userMapper;

    @Mock
    private UserRepository userRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private UserServiceImpl userService;

    @Test
    void shouldRegisterUserSuccessfully() {

        RegisterRequest request = RegisterRequest.builder()
                .name("Harleen")
                .email("harleen@gmail.com")
                .password("password123")
                .build();

        User user = User.builder()
                .name("Harleen")
                .email("harleen@gmail.com")
                .build();

        User savedUser = User.builder()
                .id(1L)
                .name("Harleen")
                .email("harleen@gmail.com")
                .role(Role.ADMIN)
                .password("encodedPassword")
                .build();

        UserResponse response = UserResponse.builder()
                .id(1L)
                .name("Harleen")
                .email("harleen@gmail.com")
                .role(Role.ADMIN)
                .build();

        when(userMapper.toEntity(request)).thenReturn(user);
        when(passwordEncoder.encode("password123")).thenReturn("encodedPassword");
        when(userRepository.save(user)).thenReturn(savedUser);
        when(userMapper.toResponse(savedUser)).thenReturn(response);

        UserResponse result = userService.register(request);

        assertNotNull(result);
        assertEquals(1L, result.getId());
        assertEquals(Role.ADMIN, result.getRole());

        verify(passwordEncoder).encode("password123");
        verify(userRepository).save(user);
    }

    @Test
    void shouldReturnUserById() {

        User user = User.builder()
                .id(1L)
                .name("Harleen")
                .build();

        when(userRepository.findById(1L))
                .thenReturn(Optional.of(user));

        User result = userService.getUserById(1L);

        assertEquals(user, result);

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldThrowExceptionWhenUserIdNotFound() {

        when(userRepository.findById(1L))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserById(1L));

        assertEquals("User not found", ex.getMessage());

        verify(userRepository).findById(1L);
    }

    @Test
    void shouldReturnUserByEmail() {

        User user = User.builder()
                .id(1L)
                .email("harleen@gmail.com")
                .build();

        when(userRepository.findByEmail("harleen@gmail.com"))
                .thenReturn(Optional.of(user));

        User result = userService.getUserByEmail("harleen@gmail.com");

        assertEquals(user, result);

        verify(userRepository).findByEmail("harleen@gmail.com");
    }

    @Test
    void shouldThrowExceptionWhenEmailNotFound() {

        when(userRepository.findByEmail("harleen@gmail.com"))
                .thenReturn(Optional.empty());

        ResourceNotFoundException ex = assertThrows(
                ResourceNotFoundException.class,
                () -> userService.getUserByEmail("harleen@gmail.com"));

        assertEquals("User not found", ex.getMessage());

        verify(userRepository).findByEmail("harleen@gmail.com");
    }
}
