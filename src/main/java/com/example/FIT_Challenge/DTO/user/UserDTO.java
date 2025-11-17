package com.example.FIT_Challenge.DTO.user;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
    private Long id ;
 private  String email ;
    private  String fullName ;
    private String role ;
}
