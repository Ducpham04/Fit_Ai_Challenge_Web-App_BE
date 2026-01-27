package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name="user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="user_id")
    private Long id;
    @Column(name="user_name")
    private String userName ;
    @Column(name="email")
    private String email ;

    @Column(name="password")
    private String password ;

    @OneToMany(mappedBy = "user", cascade = CascadeType.ALL)
    private List<InformationBodyUser> informationbodyuser;

    @ManyToOne
    @JoinColumn(name="role_id")
    private Role role;


    @Column(name="link_image")
    private String linkImage ;

    @Column(name ="create_at")
    private Date CreateAt ;
    
    @Column(name="updated_at")
    private Date updatedAt;
    
    @Column(name="last_login_at")
    private Date lastLoginAt;
    
    @Column(name="points")
    private Integer points ;
    @Column(name="is_active")
    private String status ;
    
    @PreUpdate
    protected void onUpdate() {
        updatedAt = new Date();
    }
}

