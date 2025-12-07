package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

/**
 * Entity: Report
 * 👉 Chức năng: Lưu trữ các khiếu nại/flag của người dùng về kết quả đánh giá AI
 * hoặc các vấn đề khác trong hệ thống.
 */
@Entity
@Table(name = "reports")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Report {

    /**
     * 🔑 Mã báo cáo (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "report_id")
    private Long reportId;

    /**
     * 👤 Người dùng báo cáo (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 🎯 Thử thách liên quan (khóa ngoại → challenges.challenge_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;

    /**
     * 📹 Bản ghi tham gia thử thách (khóa ngoại → user_challenges.uc_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_challenge_id")
    private UserChallenge userChallenge;

    /**
     * 🤖 Log đánh giá AI (khóa ngoại → ai_evaluation_logs.ael_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "ai_evaluation_log_id")
    private AiEvaluationLog aiEvaluationLog;

    /**
     * 🏷️ Loại báo cáo
     * - ai_incorrect: Kết quả AI không chính xác
     * - ai_unfair: Đánh giá AI không công bằng
     * - score_wrong: Điểm số sai
     * - video_issue: Vấn đề với video
     * - system_bug: Lỗi hệ thống
     * - other: Khác
     */
    @Column(name = "report_type", length = 50, nullable = false)
    private String reportType;

    /**
     * 📝 Tiêu đề báo cáo
     */
    @Column(name = "title", length = 200, nullable = false)
    private String title;

    /**
     * 📄 Mô tả chi tiết vấn đề
     */
    @Column(name = "description", columnDefinition = "TEXT", nullable = false)
    private String description;

    /**
     * 📎 Đường dẫn đến bằng chứng (hình ảnh, video, v.v.)
     */
    @Column(name = "evidence_url", columnDefinition = "TEXT")
    private String evidenceUrl;

    /**
     * ✅ Trạng thái xử lý
     * - pending: Đang chờ xử lý
     * - under_review: Đang xem xét
     * - resolved: Đã giải quyết
     * - rejected: Từ chối
     * - closed: Đã đóng
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "pending";

    /**
     * 👨‍💼 Người xử lý (admin/staff) (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "assigned_to_user_id")
    private User assignedTo;

    /**
     * 💬 Phản hồi từ admin/staff
     */
    @Column(name = "admin_response", columnDefinition = "TEXT")
    private String adminResponse;

    /**
     * 🔄 Kết quả xử lý
     * - score_adjusted: Đã điều chỉnh điểm
     * - ai_re_evaluated: Đã đánh giá lại bằng AI
     * - manual_review: Đã xem xét thủ công
     * - no_action: Không có hành động
     */
    @Column(name = "resolution_action", length = 50)
    private String resolutionAction;

    /**
     * ⭐ Điểm số mới (nếu đã điều chỉnh)
     */
    @Column(name = "adjusted_score")
    private Integer adjustedScore;

    /**
     * 🕒 Thời điểm xử lý
     */
    @Column(name = "resolved_at")
    private ZonedDateTime resolvedAt;

    /**
     * 🕒 Thời điểm tạo báo cáo
     */
    @Column(name = "created_at", nullable = false)
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


