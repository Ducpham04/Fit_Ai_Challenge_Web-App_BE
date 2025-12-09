package com.example.fitchallenge.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String refreshToken;
    private String type = "Bearer";
    private UserInfoDTO user;
    
    // Constructor for backward compatibility
    public JwtResponse(String token) {
        this.token = token;
    }
    
    public JwtResponse(String token, String refreshToken, UserInfoDTO user) {
        this.token = token;
        this.refreshToken = refreshToken;
        this.user = user;
    }
    
    // Inner class for user info in response
    @Data
    @AllArgsConstructor
    @NoArgsConstructor
    public static class UserInfoDTO {
        private Long id;
        private String email;
        private String fullName;
        private String role;
    }
}
