package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

/**
 * Entity: AiEvaluationLog
 * 👉 Chức năng: Lưu log chi tiết về quá trình xử lý và đánh giá của AI model
 * cho các video tập luyện của người dùng.
 */
@Entity
@Table(name = "ai_evaluation_logs")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class AiEvaluationLog {

    /**
     * 🔑 Mã log (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "ael_id")
    private Long aelId;

    /**
     * 👤 Người dùng (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 🎯 Thử thách được đánh giá (khóa ngoại → challenges.challenge_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id", nullable = false)
    private Challenges challenge;

    /**
     * 📹 Bản ghi tham gia thử thách (khóa ngoại → user_challenges.uc_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_challenge_id")
    private UserChallenge userChallenge;

    /**
     * 🤖 Tên model AI được sử dụng
     * Ví dụ: "pose_estimation_v1", "movement_analyzer_v2"
     */
    @Column(name = "model_name", length = 100)
    private String modelName;

    /**
     * 📊 Phiên bản model
     * Ví dụ: "1.0.0", "2.1.3"
     */
    @Column(name = "model_version", length = 50)
    private String modelVersion;

    /**
     * 🎬 Đường dẫn video đầu vào
     */
    @Column(name = "input_video_url", columnDefinition = "TEXT")
    private String inputVideoUrl;

    /**
     * 📥 Dữ liệu đầu vào (JSON)
     * Chứa metadata về video: duration, resolution, format, etc.
     */
    @Column(name = "input_payload", columnDefinition = "JSON")
    private String inputPayload;

    /**
     * 📤 Dữ liệu đầu ra từ AI (JSON)
     * Chứa kết quả phân tích: keypoints, scores, confidence, etc.
     */
    @Column(name = "output_payload", columnDefinition = "JSON")
    private String outputPayload;

    /**
     * ✅ Trạng thái xử lý
     * - pending: đang chờ xử lý
     * - processing: đang xử lý
     * - completed: hoàn thành
     * - failed: thất bại
     * - timeout: quá thời gian
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "pending";

    /**
     * 📈 Điểm số đánh giá (0-100)
     */
    @Column(name = "score")
    private Integer score;

    /**
     * 🎯 Độ tin cậy (0.0 - 1.0)
     */
    @Column(name = "confidence")
    private Double confidence;

    /**
     * ⏱️ Thời gian xử lý (milliseconds)
     */
    @Column(name = "processing_time_ms")
    private Long processingTimeMs;

    /**
     * ❌ Thông báo lỗi (nếu có)
     */
    @Column(name = "error_message", columnDefinition = "TEXT")
    private String errorMessage;

    /**
     * 📝 Ghi chú hoặc metadata bổ sung
     */
    @Column(name = "notes", columnDefinition = "TEXT")
    private String notes;

    /**
     * 🕒 Thời điểm bắt đầu xử lý
     */
    @Column(name = "started_at")
    private ZonedDateTime startedAt;

    /**
     * 🕒 Thời điểm hoàn thành xử lý
     */
    @Column(name = "completed_at")
    private ZonedDateTime completedAt;

    /**
     * 🕒 Thời điểm tạo log
     */
    @Column(name = "created_at")
    @Builder.Default
    private ZonedDateTime createdAt = ZonedDateTime.now();
}





