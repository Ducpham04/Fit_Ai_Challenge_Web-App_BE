package com.example.FIT_Challenge.DTO.user;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class JwtResponse {
    private String token;
    private String type = "Bearer" ;

    public JwtResponse(String token) {
        this.token = token;
    }
}
