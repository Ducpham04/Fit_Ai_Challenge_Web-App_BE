package com.example.fitchallenge.Entity;



import jakarta.persistence.*;
import lombok.*;
import java.time.OffsetDateTime;
import java.util.Date;

/**
 * Bảng rewards:
 * Dùng để lưu thông tin các phần thưởng mà người dùng có thể đổi bằng điểm.
 * Mỗi phần thưởng có tên, mô tả, chi phí điểm và số lượng tồn kho.
 */
@Entity
@Table(name = "rewards")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Reward {

    /**
     * 🔑 Mã phần thưởng (Primary Key)
     */
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "reward_id")
    private Long rewardId;
    /**
     * 🖼️ Link hình ảnh đại diện phần thưởng (tùy chọn)
     */
    @Column(name = "link_image")
    private String linkImage;
    /**
     * 🎁 Tên phần thưởng (bắt buộc)
     */
    @Column(nullable = false, length = 200)
    private String name;

    /**
     * 📜 Mô tả chi tiết phần thưởng (tùy chọn)
     */


    private String description;

    /**
     * 💰 Số điểm cần để đổi phần thưởng (bắt buộc)
     */
    @Column(name = "cost_points")
    private Integer costPoints;

    /**
     * 📦 Số lượng phần thưởng còn trong kho (mặc định 0)
     */
    @Column(nullable = false)
    @Builder.Default
    private Integer stock = 0;

    /**
     * 🤝 Đối tác hoặc nhà cung cấp phần thưởng (tùy chọn)
     */
    @Column(name = "external_partner", length = 200)
    private String externalPartner;

    /**
     * 🕒 Ngày tạo phần thưởng (tự động mặc định = now())
     */
    @Column(name = "created_at")
    @Builder.Default
    private OffsetDateTime createdAt = OffsetDateTime.now();
    @Column(name = "expire_At")
    private Date expireAt;
    
    @Column (name = "claimed")
    @Builder.Default
    private Integer claimed = 0;
    
    /**
     * ⚙️ Trạng thái phần thưởng: active, inactive
     */
    @Column(name = "status", length = 20)
    @Builder.Default
    private String status = "active";
}

