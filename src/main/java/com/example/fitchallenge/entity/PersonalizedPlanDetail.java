package com.example.fitchallenge.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

/**
 * Entity: PersonalizedPlanDetail
 * 👉 Chức năng: Lưu trữ bài tập đã được cá nhân hóa cho từng user
 * Dựa trên thông tin từ Health Profile, hệ thống sẽ chọn template phù hợp
 * và tạo plan cá nhân hóa với các bài tập từ training plan
 * 
 * Video sẽ được lấy từ Challenge entity thông qua challengeId
 */
@Entity
@Table(name = "personalized_plan_detail")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class PersonalizedPlanDetail {

    /**
     * 🔑 Mã bản ghi (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ppd_id")
    private Long id;

    /**
     * 🔗 Reference đến TrainingPlanDetail template (nullable, optional)
     * Dùng để track xem personalized detail này được tạo từ template nào
     * 
     * ⚠️ QUAN TRỌNG: Field này KHÔNG phải auto increment
     * Giá trị được set từ template.getTpdId() khi tạo PersonalizedPlanDetail
     * Dùng để UI hiển thị đúng challenge gốc và map với template
     */
    @Column(name = "tpd_id", nullable = true)
    private Long tpdId;

    /**
     * 🔗 Reference đến UserTraining (ut_id)
     * Dùng để track xem personalized detail này thuộc UserTraining nào
     * 
     * ⚠️ QUAN TRỌNG: Field này KHÔNG phải auto increment
     * Giá trị được set từ userTraining.getUtId() khi tạo PersonalizedPlanDetail
     */
    @Column(name = "ut_id", nullable = true)
    private Long utId;

    /**
     * 👤 Người dùng (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 📅 Số ngày trong kế hoạch (Day 1, Day 2, ...)
     */
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    /**
     * 🎯 Thử thách (khóa ngoại → challenges.challenge_id)
     * Dùng để lấy video từ Challenge entity
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenges challenge;

    /**
     * 🏋️ Tên bài tập
     * Ví dụ: "Push Up", "Squat", "Pull Up"
     */
    @Column(name = "exercise_name", nullable = false, length = 200)
    private String exerciseName;

    /**
     * 🔁 Số hiệp (sets)
     */
    @Column(name = "sets", nullable = false)
    private Integer sets;

    /**
     * 🔂 Số lần lặp trong mỗi hiệp (reps)
     */
    @Column(name = "reps", nullable = false)
    private Integer reps;

    /**
     * 📊 Độ khó
     * Ví dụ: "EASY", "MEDIUM", "HARD"
     */
    @Column(name = "difficulty", length = 50, nullable = false)
    private String difficulty;

    /**
     * 💪 Nhóm cơ mục tiêu
     * Ví dụ: "Chest", "Legs", "Back", "Arms", "Core"
     */
    @Column(name = "target_muscle", length = 100)
    private String targetMuscle;

    /**
     * 🔥 Calories ước tính cho bài tập này (dựa trên sets, reps, duration)
     * Được tính tự động khi tạo personalized plan
     */
    @Column(name = "estimated_calories")
    private Integer estimatedCalories;
}


