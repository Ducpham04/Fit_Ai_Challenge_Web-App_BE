package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.user.JwtResponse;
import com.example.fitchallenge.DTO.user.LoginRequest;
import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.UserDTO;
import com.example.fitchallenge.DTO.user.userProfile.FullUserProfileDTO;
import com.example.fitchallenge.DTO.user.userProfile.UserProfileDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.security.core.userdetails.UserDetails;

public interface UserService {
    void register(RegisterRequestAdmin registerRequestAdmin);
    JwtResponse login(LoginRequest loginRequest);
    UserDetails loadUserByEmail(String username);
    NotificationResponse getAllUsers();
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email) ;
    UserProfileDTO getUserProfile(Long userId);
    FullUserProfileDTO getFullUserProfile(Long userId);
}
