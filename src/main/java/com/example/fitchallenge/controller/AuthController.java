package com.example.fitchallenge.controller;


import com.example.fitchallenge.DTO.user.LoginRequest;
import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.JwtResponse;
import com.example.fitchallenge.DTO.user.UserDTO;
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
public class AuthController {

    private final UserService userService;

    @PostMapping("/auth/register")
    public ResponseEntity<JwtResponse> register(@RequestBody RegisterRequestAdmin request) {
        JwtResponse jwtResponse = userService.register(request);
        return ResponseEntity.ok(jwtResponse);
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
    @GetMapping("/admin/users")
    public ResponseEntity<NotificationResponse> getAllUsers() {
        NotificationResponse response = userService.getAllUsers();
        System.out.println("Da vao get all users");
        return ResponseEntity.ok(response);
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


}
