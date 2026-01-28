package com.example.fitchallenge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name="Goals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Goals {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="goal_id")
    private Long id ;
    @Column(name="Image_Link")
    private String imageLink ;
    @Column(name="Name")
    private String name;
    @Column(name="Descriptions")
    private String description ;

}
