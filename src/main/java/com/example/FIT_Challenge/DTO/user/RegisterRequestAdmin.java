package com.example.FIT_Challenge.DTO.user;

import lombok.Data;

@Data
public class RegisterRequestAdmin {
    private String fullName; // dùng thay cho fullname
    private String email;
    private String password;
    private Long roleId;
}
