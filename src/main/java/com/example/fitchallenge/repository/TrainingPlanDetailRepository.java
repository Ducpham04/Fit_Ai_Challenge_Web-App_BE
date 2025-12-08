package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.TrainingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanDetailRepository extends JpaRepository<TrainingPlanDetail, Long> {
    List<TrainingPlanDetail> findByTrainingPlan_TpId(Long tpId);
    List<TrainingPlanDetail> findByTrainingPlan_TpIdAndDayNumber(Long tpId, Integer dayNumber);
    
    /**
     * Tìm TrainingPlanDetail theo trainingPlanId và challengeId
     */
    java.util.Optional<TrainingPlanDetail> findByTrainingPlan_TpIdAndChallenge_Id(Long tpId, Long challengeId);
}
