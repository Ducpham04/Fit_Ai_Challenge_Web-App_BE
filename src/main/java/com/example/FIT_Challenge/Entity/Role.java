package com.example.FIT_Challenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Role_user")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @Column(name="Role_id")
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name="Role_name")
    private String roleName;

    @Column(name="Description")
    private String  description;

    @OneToMany(mappedBy = "role", cascade = CascadeType.ALL)
    private java.util.List<User> users;


}
