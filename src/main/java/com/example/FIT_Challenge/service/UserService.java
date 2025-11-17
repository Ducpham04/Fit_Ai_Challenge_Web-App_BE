package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.user.JwtResponse;
import com.example.FIT_Challenge.DTO.user.LoginRequest;
import com.example.FIT_Challenge.DTO.user.RegisterRequestAdmin;
import com.example.FIT_Challenge.DTO.user.UserDTO;
import com.example.FIT_Challenge.config.NotificationResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void register(RegisterRequestAdmin registerRequestAdmin);
    JwtResponse login(LoginRequest loginRequest);
    UserDetails loadUserByEmail(String username);
    NotificationResponse getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email) ;
}
