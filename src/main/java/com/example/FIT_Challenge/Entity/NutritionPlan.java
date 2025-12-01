package com.example.FIT_Challenge.Entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.OffsetDateTime;
import java.util.List;

@Entity

@Table(name = "nutrition_plans")
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class NutritionPlan {

    // 🆔 Mã kế hoạch dinh dưỡng (tự động tăng)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "plan_id")
    private Long planId;

    // 🎯 Mục tiêu liên quan (liên kết đến bảng goals)
    @ManyToOne
    @JoinColumn(name = "goal_id", referencedColumnName = "goal_id")
    private Goals goal;

    // 📋 Tiêu đề kế hoạch (bắt buộc)
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    // 📝 Mô tả chi tiết kế hoạch
    @Column(name = "description")
    private String description;

    // 🔥 Lượng calo/ngày (có thể null)
    @Column(name = "calories_per_day")
    private Integer caloriesPerDay;

    // 🍗 Lượng protein (gram)
    @Column(name = "protein_g")
    private Integer proteinG;

    // 🍚 Lượng carbohydrate (gram)
    @Column(name = "carbs_g")
    private Integer carbsG;

    // 🧈 Lượng chất béo (gram)
    @Column(name = "fat_g")
    private Integer fatG;

    // ⚙️ Trạng thái kế hoạch (mặc định là "active")
    @Column(name = "status", length = 20)
    private String status = "active";

    // 🕒 Ngày tạo kế hoạch (tự động lưu thời gian hiện tại)
    @Column(name = "created_at")
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @OneToMany(mappedBy = "nutritionPlan", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<Meal> meals;




}
