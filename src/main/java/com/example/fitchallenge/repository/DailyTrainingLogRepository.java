package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.DailyTrainingLog;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface DailyTrainingLogRepository extends JpaRepository<DailyTrainingLog, Long> {
    
    /**
     * Lấy tất cả daily training logs của một user
     */
    List<DailyTrainingLog> findByUser_Id(Long userId);
    
    /**
     * Lấy daily training logs của một user trong một training plan
     */
    List<DailyTrainingLog> findByUser_IdAndTrainingPlan_TpId(Long userId, Long trainingPlanId);
    
    /**
     * Lấy daily training logs của một user trong một training plan theo day number
     */
    List<DailyTrainingLog> findByUser_IdAndTrainingPlan_TpIdAndDayNumber(Long userId, Long trainingPlanId, Integer dayNumber);
    
    /**
     * Lấy daily training log của một user cho một challenge cụ thể
     */
    Optional<DailyTrainingLog> findByUser_IdAndTrainingPlan_TpIdAndDayNumberAndChallenge_Id(
        Long userId, 
        Long trainingPlanId, 
        Integer dayNumber, 
        Long challengeId
    );
    
    /**
     * Lấy daily training logs theo status
     */
    List<DailyTrainingLog> findByUser_IdAndTrainingPlan_TpIdAndStatus(
        Long userId, 
        Long trainingPlanId, 
        String status
    );
    
    /**
     * Lấy daily training logs theo training date
     */
    List<DailyTrainingLog> findByUser_IdAndTrainingDate(Long userId, LocalDate trainingDate);
    
    /**
     * Đếm số daily training logs đã completed của một user trong một training plan
     */
    long countByUser_IdAndTrainingPlan_TpIdAndStatus(Long userId, Long trainingPlanId, String status);
}

