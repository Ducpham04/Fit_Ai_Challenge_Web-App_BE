package com.example.FIT_Challenge.Entity;

import com.example.FIT_Challenge.Entity.TrainingPlan;
import com.example.FIT_Challenge.Entity.User;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;

@Setter
@Getter
@Entity
@Table(name = "user_training")
public class UserTraining {

    // 🧩 Getters và Setters
    // 🆔 Mã bản ghi người dùng - kế hoạch
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ut_id")
    private Long utId;

    // 👤 Người dùng tham gia kế hoạch
    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    // 🏋️‍♂️ Kế hoạch tập mà người dùng đang theo
    @ManyToOne
    @JoinColumn(name = "tp_id")
    private TrainingPlan trainingPlan;

    // 📅 Ngày bắt đầu kế hoạch
    @Column(name = "start_date")
    private LocalDate startDate;

    // 📆 Ngày kết thúc kế hoạch (có thể null)
    @Column(name = "end_date")
    private LocalDate endDate;
    // ❌ THIẾU: Số ngày đã hoàn thành
    @Column(name = "completed_days")
    private Integer completedDays = 0;

    // ❌ THIẾU: Ngày hiện tại đang ở (để tracking)
    @Column(name = "current_day")
    private Integer currentDay = 1;

    // ❌ THIẾU: Tỷ lệ hoàn thành (%)
    @Column(name = "completion_percentage")
    private Double completionPercentage = 0.0;

    // ⚙️ Trạng thái kế hoạch: active / completed / cancelled
    @Column(name = "status", length = 20, columnDefinition = "VARCHAR(20) DEFAULT 'active'")
    private String status = "active";

    // 🧱 Constructors
    public UserTraining() {}

    public UserTraining(User user, TrainingPlan trainingPlan, LocalDate startDate, LocalDate endDate, String status) {
        this.user = user;
        this.trainingPlan = trainingPlan;
        this.startDate = startDate;
        this.endDate = endDate;
        this.status = status;
    }

}
