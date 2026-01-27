package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

@Table(name = "training_plan_details")
public class TrainingPlanDetail {
    // Mô tả bảng lưu trữ kế hoạch từng ngày
    // 🆔 Mã chi tiết kế hoạch (primary key, tự động tăng)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpd_id")
    private Long tpdId;

    // 🔗 Kế hoạch gốc (một kế hoạch có nhiều chi tiết)
    @ManyToOne
    @JoinColumn(name = "tp_id")
    private TrainingPlan trainingPlan;

    // 📅 Ngày trong kế hoạch (bắt buộc nhập)
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    // 🏋️‍♂️ Thử thách gắn với ngày tập (một thử thách có thể nằm trong nhiều kế hoạch)
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;

    // 🔁 Số hiệp (sets), mặc định = 1
    @Column(name = "sets", columnDefinition = "INTEGER DEFAULT 1")
    private Integer sets = 1;

    // 🔂 Số lần lặp trong mỗi hiệp (có thể null nếu không áp dụng)
    @Column(name = "reps")
    private Integer reps;

    // ⏱️ Thời lượng tập (tính bằng giây)
    @Column(name = "duration")
    private Integer duration;

    // ⏸️ Thời gian nghỉ giữa các hiệp (tính bằng giây)
    @Column(name = "rest_time")
    private Integer restTime;

    // 📝 Hướng dẫn tập luyện
    @Column(name = "instructions", columnDefinition = "TEXT")
    private String instructions;

}