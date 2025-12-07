package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;

/**
 * Entity lưu chỉ số cơ thể của user để cá nhân hóa bài tập
 */
@Entity
@Table(name = "user_body_profile")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyProfile {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @ManyToOne
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    @Column(name = "height")
    private Double height; // cm

    @Column(name = "weight")
    private Double weight; // kg

    @Column(name = "bmi")
    private Double bmi;

    @Column(name = "body_fat")
    private Double bodyFat; // percentage

    @Column(name = "muscle_mass")
    private Double muscleMass; // kg

    @Column(name = "age")
    private Integer age;

    @Column(name = "gender", length = 20)
    private String gender; // MALE, FEMALE, OTHER

    @Column(name = "experience_level", length = 20)
    private String experienceLevel; // beginner, intermediate, advanced

    @Column(name = "goal", length = 50)
    private String goal; // lose_weight, build_muscle, maintain_fitness

    @Column(name = "injury_notes", columnDefinition = "TEXT")
    private String injuryNotes;

    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    @Column(name = "updated_at")
    private OffsetDateTime updatedAt = OffsetDateTime.now();

    @PreUpdate
    protected void onUpdate() {
        updatedAt = OffsetDateTime.now();
    }
}

