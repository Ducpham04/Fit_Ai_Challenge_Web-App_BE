package com.example.fitchallenge.controller;


import com.example.fitchallenge.dto.users.LoginRequest;
import com.example.fitchallenge.dto.users.RegisterRequestAdmin;
import com.example.fitchallenge.dto.users.UpdateProfileRequest;
import com.example.fitchallenge.dto.users.JwtResponse;
import com.example.fitchallenge.dto.users.UserDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class Auth {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<?> register(@RequestBody RegisterRequestAdmin request) {
        try {
            // Validate request
            if (request.getEmail() == null || request.getEmail().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new NotificationResponse(false, "Email is required")
                );
            }
            if (request.getFullName() == null || request.getFullName().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new NotificationResponse(false, "Full name is required")
                );
            }
            if (request.getPassword() == null || request.getPassword().trim().isEmpty()) {
                return ResponseEntity.badRequest().body(
                    new NotificationResponse(false, "Password is required")
                );
            }
            if (request.getPassword().length() < 6) {
                return ResponseEntity.badRequest().body(
                    new NotificationResponse(false, "Password must be at least 6 characters")
                );
            }
            
            JwtResponse jwtResponse = userService.register(request);
            return ResponseEntity.ok(jwtResponse);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new NotificationResponse(false, e.getMessage())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Registration failed: " + e.getMessage())
            );
        }
    }
    
    @GetMapping("/auth/user")
    public ResponseEntity<UserDTO> getUserProfile(
            @AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }
        UserDTO user = userService.getUserByEmail(userDetails.getUsername());
        return ResponseEntity.ok(user);
    }

    @PostMapping("/auth/login")
    public ResponseEntity<JwtResponse> login(@RequestBody LoginRequest request) {
        JwtResponse jwtResponse = userService.login(request);
        System.out.println("Ddax vaof");
        return ResponseEntity.ok(jwtResponse);
    }
    
    @GetMapping("/users/{id}")
    public ResponseEntity<NotificationResponse> getUserById(@PathVariable Long id) {
        NotificationResponse response = new NotificationResponse();
        try {
            response.setSuccess(true);
            response.setMessage("User found");
            response.setData(userService.getUserById(id));
        } catch (Exception e) {
            response.setSuccess(false);
            response.setMessage("User not found");
        }
        return ResponseEntity.ok(response);
    }
    @GetMapping("/auth/me")
    public ResponseEntity<UserDTO> getCurrentUser(
            @AuthenticationPrincipal UserDetails userDetails) {

        if (userDetails == null) {
            return ResponseEntity.status(401).build();
        }

        UserDTO user = userService.getUserByEmail(userDetails.getUsername());

        System.out.println("user email: " + userDetails.getUsername());
        System.out.println("user info: " + user.getEmail() + user.getFullName() ) ;
        return ResponseEntity.ok(user);
    }

    /**
     * PUT /api/auth/profile
     * Cập nhật thông tin profile của user hiện tại (username, email, avatar)
     */
    @PutMapping("/auth/profile")
    public ResponseEntity<?> updateProfile(
            @AuthenticationPrincipal UserDetails userDetails,
            @RequestBody UpdateProfileRequest request) {
        
        if (userDetails == null) {
            return ResponseEntity.status(401).body(
                new NotificationResponse(false, "Unauthorized")
            );
        }

        try {
            UserDTO user = userService.getUserByEmail(userDetails.getUsername());
            Long userId = user.getId();

            // Update user profile (username and email)
            RegisterRequestAdmin updateRequest = new RegisterRequestAdmin();
            if (request.getUserName() != null && !request.getUserName().trim().isEmpty()) {
                updateRequest.setFullName(request.getUserName().trim());
            }
            if (request.getEmail() != null && !request.getEmail().trim().isEmpty()) {
                updateRequest.setEmail(request.getEmail().trim());
            }

            UserDTO updatedUser;
            if (updateRequest.getFullName() != null || updateRequest.getEmail() != null) {
                updatedUser = userService.updateUser(userId, updateRequest);
            } else {
                updatedUser = user;
            }
            
            // Update avatar if provided
            if (request.getLinkImage() != null && !request.getLinkImage().trim().isEmpty()) {
                updatedUser = userService.updateUserAvatar(userId, request.getLinkImage().trim());
            }

            return ResponseEntity.ok(new NotificationResponse(true, "Profile updated successfully", updatedUser));
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(
                new NotificationResponse(false, e.getMessage())
            );
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body(
                new NotificationResponse(false, "Error updating profile: " + e.getMessage())
            );
        }
    }


}
