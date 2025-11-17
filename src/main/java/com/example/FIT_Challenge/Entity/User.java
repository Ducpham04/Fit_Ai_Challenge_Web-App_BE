package com.example.FIT_Challenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@Entity
@Table(name="User")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="User_Id")
    private Long id;
    @Column(name="UserName")
    private String userName ;
    @Column(name="Email")
    private String email ;

    @Column(name="Password")
    private String password ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<InformationBodyUser> informationbodyuser;

    @OneToOne
    @JoinColumn(name="Role_id", unique=true)
    private Role role;
}

