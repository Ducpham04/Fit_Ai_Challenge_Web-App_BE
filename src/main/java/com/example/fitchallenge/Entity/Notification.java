package com.example.fitchallenge.Entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.ZonedDateTime;

/**
 * Entity: Notification
 * 👉 Chức năng: Quản lý thông báo gửi đến người dùng
 * (thành tích, nhắc nhở, cập nhật, v.v.)
 */
@Entity
@Table(name = "notifications")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Notification {

    /**
     * 🔑 Mã thông báo (Primary Key, tự tăng)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "notification_id")
    private Long notificationId;

    /**
     * 👤 Người dùng nhận thông báo (khóa ngoại → users.user_id)
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 📢 Tiêu đề thông báo
     */
    @Column(name = "title", nullable = false, length = 200)
    private String title;

    /**
     * 📝 Nội dung thông báo
     */
    @Column(name = "content", columnDefinition = "TEXT", nullable = false)
    private String content;

    /**
     * 🏷️ Loại thông báo
     * - achievement: Thành tích
     * - reminder: Nhắc nhở
     * - update: Cập nhật
     * - reward: Phần thưởng
     * - challenge: Thử thách
     * - system: Hệ thống
     */
    @Column(name = "type", length = 50, nullable = false)
    private String type;

    /**
     * 🔗 Link liên kết (nếu có)
     * Ví dụ: link đến challenge, reward, profile, etc.
     */
    @Column(name = "link_url", columnDefinition = "TEXT")
    private String linkUrl;

    /**
     * 🖼️ Icon hoặc hình ảnh thông báo
     */
    @Column(name = "icon_url", columnDefinition = "TEXT")
    private String iconUrl;

    /**
     * ✅ Trạng thái đọc
     * - unread: Chưa đọc
     * - read: Đã đọc
     */
    @Column(name = "is_read", nullable = false)
    @Builder.Default
    private Boolean isRead = false;

    /**
     * 📅 Thời điểm đọc (nếu đã đọc)
     */
    @Column(name = "read_at")
    private ZonedDateTime readAt;

    /**
     * 📤 Trạng thái gửi
     * - pending: Đang chờ gửi
     * - sent: Đã gửi
     * - failed: Gửi thất bại
     */
    @Column(name = "send_status", length = 20)
    @Builder.Default
    private String sendStatus = "pending";

    /**
     * 📧 Đã gửi qua email
     */
    @Column(name = "sent_via_email")
    @Builder.Default
    private Boolean sentViaEmail = false;

    /**
     * 📱 Đã gửi qua push notification
     */
    @Column(name = "sent_via_push")
    @Builder.Default
    private Boolean sentViaPush = false;

    /**
     * 🔔 Đã gửi qua in-app notification
     */
    @Column(name = "sent_via_in_app")
    @Builder.Default
    private Boolean sentViaInApp = true;

    /**
     * 🕒 Thời điểm gửi
     */
    @Column(name = "sent_at")
    private ZonedDateTime sentAt;

    /**
     * 🕒 Thời điểm tạo thông báo
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


