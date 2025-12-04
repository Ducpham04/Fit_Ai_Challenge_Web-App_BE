package com.example.fitchallenge.Entity;



import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.ZonedDateTime;

/**
 * Entity: RewardRedemption
 * 👉 Chức năng: Lưu thông tin người dùng đổi quà trong hệ thống.
 * Mỗi bản ghi thể hiện một giao dịch đổi quà giữa người dùng và phần thưởng cụ thể.
 */
@Setter
@Getter
@Entity
@Table(name = "reward_redemptions")
public class RewardRedemption {

    /**
     * 🔑 Mã giao dịch đổi quà (Primary Key, tự tăng).
     * Dùng để định danh duy nhất mỗi giao dịch.
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "rr_id")
    private Long id;

    /**
     * 👤 Người dùng thực hiện đổi quà.
     * Liên kết với bảng users thông qua khóa ngoại user_id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", nullable = false)
    private User user;

    /**
     * 🎁 Phần thưởng được đổi.
     * Liên kết với bảng rewards thông qua khóa ngoại reward_id.
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "reward_id", nullable = false)
    private Reward reward;

    /**
     * 📦 Trạng thái giao dịch đổi quà.
     * Có thể là: 'pending' (đang xử lý), 'fulfilled' (hoàn tất), 'cancelled' (hủy).
     * Mặc định là 'pending'.
     */
    @Column(name = "status", length = 20, nullable = false)
    private String status = "pending";

    /**
     * 🕒 Ngày yêu cầu đổi quà.
     * Mặc định là thời điểm hiện tại (now()).
     */
    @Column(name = "created_at")
    private ZonedDateTime createdAt = ZonedDateTime.now();

    /**
     * ✅ Ngày hoàn tất giao dịch (nếu có).
     * Có thể để trống nếu chưa xử lý hoặc bị hủy.
     */
    @Column(name = "fulfilled_at")
    private ZonedDateTime fulfilledAt;

    // ===== Getters và Setters =====

}
