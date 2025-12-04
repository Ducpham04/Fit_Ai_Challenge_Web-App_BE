package com.example.fitchallenge.Entity;


import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;

/**
 * Bảng user_nutrition:
 * Dùng để lưu thông tin người dùng theo các kế hoạch dinh dưỡng.
 * Một user có thể theo nhiều nutrition_plan trong thời gian khác nhau.
 */
@Entity
@Table(name = "user_nutrition")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserNutrition {

    /**
     * 🔑 Mã liên kết người dùng - kế hoạch (PRIMARY KEY)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "un_id")
    private Long unId;

    /**
     * 👤 Người dùng (FK → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 🥗 Kế hoạch dinh dưỡng (FK → nutrition_plans.plan_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private NutritionPlan nutritionPlan;

    /**
     * 📅 Ngày bắt đầu kế hoạch (bắt buộc)
     */
    @Column(name = "start_date", nullable = false)
    private LocalDate startDate;

    /**
     * 📅 Ngày kết thúc kế hoạch (có thể null nếu chưa kết thúc)
     */
    @Column(name = "end_date")
    private LocalDate endDate;
    @Column(name ="completion")
    private Double completion = 0.0;
    /**
     * ⚙️ Trạng thái kế hoạch: active / completed / canceled / paused
     */
    @Column(name = "status", length = 20)
    private String status = "active";
}

