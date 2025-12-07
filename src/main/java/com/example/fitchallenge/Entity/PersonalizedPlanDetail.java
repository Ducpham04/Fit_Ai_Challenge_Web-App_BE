package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity lưu reps/time/exercise tùy chỉnh theo từng user
 */
@Entity
@Table(name = "personalized_plan_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class PersonalizedPlanDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppd_id")
    private Long ppdId;

    @ManyToOne
    @JoinColumn(name = "ut_id", nullable = false)
    private UserTraining userTraining; // FK to UserTraining (ut_id)

    @ManyToOne
    @JoinColumn(name = "tpd_id", nullable = false)
    private TrainingPlanDetail trainingPlanDetail; // FK to TrainingPlanDetail (tpd_id)

    @Column(name = "custom_reps")
    private Integer customReps;

    @Column(name = "custom_time")
    private Integer customTime; // seconds

    @Column(name = "custom_distance")
    private Double customDistance; // km

    @Column(name = "exercise_variant", length = 200)
    private String exerciseVariant; // ví dụ: "burpee low-impact"

    @Column(name = "intensity_level")
    private Integer intensityLevel; // 1-10

    @Column(name = "note", columnDefinition = "TEXT")
    private String note;
}

