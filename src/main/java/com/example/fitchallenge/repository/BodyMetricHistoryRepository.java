package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.BodyMetricHistory;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

public interface BodyMetricHistoryRepository extends JpaRepository<BodyMetricHistory, Long> {
    
    /**
     * Lấy tất cả lịch sử body metric của user, sắp xếp theo ngày ghi nhận (mới nhất trước)
     */
    List<BodyMetricHistory> findByUserIdOrderByRecordedAtDesc(Long userId);
    
    /**
     * Lấy lịch sử body metric trong khoảng thời gian
     */
    @Query("SELECT b FROM BodyMetricHistory b WHERE b.user.id = :userId " +
           "AND b.recordedAt >= :fromDate AND b.recordedAt <= :toDate " +
           "ORDER BY b.recordedAt ASC")
    List<BodyMetricHistory> findByUserIdAndDateRange(
            @Param("userId") Long userId,
            @Param("fromDate") ZonedDateTime fromDate,
            @Param("toDate") ZonedDateTime toDate
    );
    
    /**
     * Lấy bản ghi mới nhất của user (Top 1)
     */
    @Query(value = "SELECT * FROM body_metric_history WHERE user_id = :userId " +
           "ORDER BY recorded_at DESC LIMIT 1", nativeQuery = true)
    Optional<BodyMetricHistory> findLatestByUserId(@Param("userId") Long userId);
}

