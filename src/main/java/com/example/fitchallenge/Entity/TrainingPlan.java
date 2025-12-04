package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.OffsetDateTime;

@Setter
@Getter
@Entity
@Table(name = "training_plans")
public class TrainingPlan {

    // 🧩 Getters và Setters
    // 🆔 Khóa chính tự động tăng
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tp_id")
    private Long tpId;

    // 🔗 Liên kết với bảng goals (một kế hoạch thuộc một mục tiêu)
    @ManyToOne
    @JoinColumn(name = "goal_id")
    private Goals goal;

    // 🏋️ Tiêu đề của kế hoạch (bắt buộc nhập)
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    // 📝 Mô tả chi tiết kế hoạch (có thể để trống)
    @Column(name = "description")
    private String description;

    // ⚙️ Mức độ khó của kế hoạch: dễ, trung bình, khó...
    @Column(name = "difficulty_level", length = 20)
    private String difficultyLevel;

    // 📆 Thời lượng kế hoạch (tính theo tuần)
    @Column(name = "duration_weeks")
    private Integer durationWeeks;

    // 🕒 Thời điểm tạo kế hoạch (mặc định là thời gian hiện tại)
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();

    // 🧱 Constructors
    public TrainingPlan() {}

    public TrainingPlan(Goals goal, String title, String description, String difficultyLevel, Integer durationWeeks) {
        this.goal = goal;
        this.title = title;
        this.description = description;
        this.difficultyLevel = difficultyLevel;
        this.durationWeeks = durationWeeks;
    }

}
