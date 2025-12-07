package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.math.BigDecimal;
import java.time.ZonedDateTime;

/**
 * Entity: BodyMetricHistory
 * 👉 Chức năng: Lưu lịch sử các chỉ số cơ thể của người dùng theo thời gian
 * (cân nặng, BMI, body fat, v.v.) để theo dõi tiến độ.
 */
@Entity
@Table(name = "body_metric_history")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class BodyMetricHistory {

    /**
     * 🔑 Mã bản ghi lịch sử (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "bmh_id")
    private Long bmhId;

    /**
     * 👤 Người dùng (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * ⚖️ Cân nặng (kg)
     * Precision: 6, Scale: 2 (ví dụ: 75.50 kg)
     */
    @Column(name = "weight_kg", precision = 6, scale = 2, nullable = false)
    private BigDecimal weightKg;

    /**
     * 📏 Chiều cao (cm)
     * Precision: 5, Scale: 2 (ví dụ: 175.00 cm)
     */
    @Column(name = "height_cm", precision = 5, scale = 2)
    private BigDecimal heightCm;

    /**
     * 📊 Chỉ số BMI (Body Mass Index)
     * Precision: 5, Scale: 2 (ví dụ: 24.50)
     */
    @Column(name = "bmi", precision = 5, scale = 2)
    private BigDecimal bmi;

    /**
     * 🎯 Tỷ lệ mỡ cơ thể (%)
     * Precision: 5, Scale: 2 (ví dụ: 15.50%)
     */
    @Column(name = "body_fat_pct", precision = 5, scale = 2)
    private BigDecimal bodyFatPct;

    /**
     * 💪 Khối lượng cơ (kg)
     * Precision: 6, Scale: 2
     */
    @Column(name = "muscle_mass_kg", precision = 6, scale = 2)
    private BigDecimal muscleMassKg;

    /**
     * 💧 Tỷ lệ nước trong cơ thể (%)
     * Precision: 5, Scale = 2
     */
    @Column(name = "water_pct", precision = 5, scale = 2)
    private BigDecimal waterPct;

    /**
     * 📝 Ghi chú hoặc mô tả thêm
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * 📅 Ngày ghi nhận chỉ số
     * Mặc định là thời điểm hiện tại
     */
    @Column(name = "recorded_at", nullable = false)
    @Builder.Default
    private ZonedDateTime recordedAt = ZonedDateTime.now();

    /**
     * 🕒 Ngày tạo bản ghi
     */
    @Column(name = "created_at")
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}


