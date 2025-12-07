package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.user.JwtResponse;
import com.example.fitchallenge.DTO.user.LoginRequest;
import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.UserDTO;
import com.example.fitchallenge.DTO.user.userProfile.FullUserProfileDTO;
import com.example.fitchallenge.DTO.user.userProfile.UserProfileDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.security.core.userdetails.UserDetails;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface UserService {
    JwtResponse register(RegisterRequestAdmin registerRequestAdmin);
    JwtResponse login(LoginRequest loginRequest);
    UserDetails loadUserByEmail(String username);
    NotificationResponse getAllUsers();
    Page<UserDTO> getAllUsersPaginated(String status, String role, String search, Pageable pageable);
    UserDTO getUserById(Long id);
    UserDTO getUserByEmail(String email) ;
    UserProfileDTO getUserProfile(Long userId);
    FullUserProfileDTO getFullUserProfile(Long userId);
    UserDTO createUser(RegisterRequestAdmin request);
    UserDTO updateUser(Long id, RegisterRequestAdmin request);
    NotificationResponse deleteUser(Long id);
}
