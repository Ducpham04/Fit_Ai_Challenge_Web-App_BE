package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="goal_id")
    private Long id ;
    @Column(name="image_link")
    private String imageLink ;
    @Column(name="name")
    private String name;
    @Column(name="descriptions")
    private String description ;

}
