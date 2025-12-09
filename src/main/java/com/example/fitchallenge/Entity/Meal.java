package com.example.fitchallenge.Entity;



import jakarta.persistence.*;
import lombok.*;

import java.util.List;

/**
 * 🥗 Entity Meal
 * Đại diện cho bảng "meals" trong cơ sở dữ liệu.
 * Mỗi Meal là một bữa ăn thuộc về một NutritionPlan.
 */
@Entity
@Table(name = "meals")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Meal {

    /**
     * 🔑 meal_id: Khóa chính (PRIMARY KEY)
     * Dùng để định danh duy nhất cho mỗi bữa ăn.
     * Tự động tăng nhờ cơ chế SERIAL trong PostgreSQL/MySQL.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "meal_id")
    private Long mealId;

    /**
     * 🔗 plan_id: Khóa ngoại (FOREIGN KEY)
     * Liên kết tới NutritionPlan (bảng nutrition_plans).
     * Một kế hoạch dinh dưỡng có thể có nhiều Meal.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "plan_id", nullable = false)
    private NutritionPlan nutritionPlan;

    /**
     * 🍽 meal_type: Loại bữa ăn
     * Ví dụ: "breakfast", "lunch", "dinner", "snack".
     * Bắt buộc phải có (NOT NULL).
     */
    @Column(name = "meal_type", nullable = false, length = 50)
    private String mealType;

    /**
     * 🥗 name: Tên món ăn
     * Ví dụ: "Grilled Chicken Salad".
     * Có thể để trống.
     */
    @Column(name = "name", length = 200)
    private String name;

    /**
     * 📖 description: Mô tả chi tiết món ăn
     * Dùng kiểu TEXT để lưu nội dung dài (công thức, thành phần, hướng dẫn...).
     */

    @OneToMany(mappedBy = "meal", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<MealFood> mealFoods;

    private String description;

    /**
     * 🔥 calories_estimate: Lượng calo ước tính của bữa ăn (tính bằng kcal).
     * Có thể để null nếu không xác định.
     */
    @Column(name = "calories_estimate")
    private Integer caloriesEstimate;

    @Column(name="Day_Number")
    private Integer day;
}
