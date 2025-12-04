package com.example.fitchallenge.DTO.user;

import lombok.*;

import java.util.Date;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id ;
 private  String email ;
    private  String fullName ;
    private String role ;
    private String linkImage ;
    private Date createdAt ;
    private String status ;
}
