package com.example.fitchallenge.Entity;



import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Entity: AiModelEvent
 * 👉 Chức năng: Lưu log kết quả và thông tin xác thực của mô hình AI khi phân tích video của người dùng.
 * Mỗi bản ghi thể hiện một lần chạy mô hình AI trên video của user_challenge.
 */
@Entity
@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "ai_model_events")
public class AiModelEvent {

    /**
     * 🔑 Mã sự kiện (Primary Key, tự tăng)
     * Dùng để định danh duy nhất mỗi lần ghi nhận kết quả AI.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ame_id")
    private Long id;

    /**
     * 🎯 Liên kết đến phiên thử thách của người dùng (user_challenges).
     * Đây là bản ghi chứa thông tin user tham gia và video được AI phân tích.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "uc_id", nullable = false)
    private UserChallenge userChallenge;

    /**
     * 🤖 Tên mô hình AI sử dụng để phân tích video.
     * Ví dụ: "PoseNet", "MoveNet", "FitAI-PushUp-Classifier".
     */
    @Column(name = "model_name", length = 200)
    private String modelName;

    /**
     * 🔢 Phiên bản mô hình AI.
     * Giúp xác định kết quả sinh ra từ version nào của mô hình (ví dụ: "v1.2.3").
     */
    @Column(name = "model_version", length = 50)
    private String modelVersion;

    /**
     * 🧠 Dữ liệu kết quả AI dưới dạng JSON.
     * Có thể bao gồm toạ độ keypoints, độ tin cậy, số lần lặp, nhãn hành động, v.v.
     * Sử dụng kiểu JSONB (PostgreSQL) để lưu trữ dữ liệu linh hoạt.
     */
    @Column(name = "result_json")
    private String resultJson;

    /**
     * 🕒 Ngày giờ ghi nhận log của mô hình AI.
     * Mặc định là thời điểm hiện tại.
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();


}

