package com.example.fitchallenge.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDate;
import java.time.ZonedDateTime;

/**
 * Entity: DailyTrainingLog
 * 👉 Chức năng: Theo dõi tiến độ tập luyện từng ngày của người dùng
 * trong kế hoạch tập luyện.
 */
@Entity
@Table(name = "daily_training_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DailyTrainingLog {

    /**
     * 🔑 Mã bản ghi (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dtl_id")
    private Long dtlId;

    /**
     * 👤 Người dùng (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 🏋️ Kế hoạch tập luyện (khóa ngoại → training_plans.tp_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tp_id", nullable = false)
    private TrainingPlan trainingPlan;

    /**
     * 📅 Ngày tập luyện
     */
    @Column(name = "training_date", nullable = false)
    private LocalDate trainingDate;

    /**
     * 📆 Số ngày trong kế hoạch (Day 1, Day 2, ...)
     */
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    /**
     * 🎯 Thử thách được thực hiện (khóa ngoại → challenges.challenge_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;

    /**
     * ✅ Trạng thái hoàn thành
     * - not_started: Chưa bắt đầu
     * - in_progress: Đang thực hiện
     * - completed: Đã hoàn thành
     * - skipped: Đã bỏ qua
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "not_started";

    /**
     * ⏱️ Thời gian tập luyện thực tế (phút)
     */
    @Column(name = "actual_duration_minutes")
    private Integer actualDurationMinutes;

    /**
     * 🔥 Calo đốt cháy (ước tính)
     */
    @Column(name = "calories_burned")
    private Integer caloriesBurned;

    /**
     * 💪 Số hiệp đã hoàn thành
     */
    @Column(name = "sets_completed")
    private Integer setsCompleted;

    /**
     * 🔂 Số lần lặp đã hoàn thành
     */
    @Column(name = "reps_completed")
    private Integer repsCompleted;

    /**
     * 📊 Điểm số đánh giá (nếu có AI evaluation)
     */
    @Column(name = "score")
    private Integer score;

    /**
     * 🎯 Độ tin cậy AI (nếu có)
     */
    @Column(name = "confidence")
    private Double confidence;

    /**
     * 📝 Ghi chú của người dùng
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * 😊 Mức độ khó cảm nhận (1-10)
     */
    @Column(name = "perceived_difficulty")
    private Integer perceivedDifficulty;

    /**
     * 💪 Mức độ nỗ lực (1-10)
     */
    @Column(name = "effort_level")
    private Integer effortLevel;

    /**
     * 🕒 Thời điểm bắt đầu tập
     */
    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    /**
     * 🕒 Thời điểm hoàn thành
     */
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    /**
     * 🕒 Thời điểm tạo bản ghi
     */
    @Column(name = "created_at")
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();

    /**
     * 🕒 Thời điểm cập nhật
     */
    @Column(name = "updated_at")
    private ZonedDateTime updatedAt;

    @PreUpdate
    protected void onUpdate() {
        updatedAt = ZonedDateTime.now();
    }
}





