package com.example.fitchallenge.dto.users;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id;
    private String email;
    private String fullName;
    private String role;
    private String linkImage;
    private String profileImage; // Alias for linkImage for FE compatibility
    private Date createdAt;
    private Date updatedAt;
    private Date lastLoginAt;
    private String status;
    
    // Constructor for backward compatibility
    public UserDTO(Long id, String email, String fullName, String role, String linkImage, Date createdAt, String status) {
        this.id = id;
        this.email = email;
        this.fullName = fullName;
        this.role = role;
        this.linkImage = linkImage;
        this.profileImage = linkImage; // Map linkImage to profileImage
        this.createdAt = createdAt;
        this.status = status;
    }
}
