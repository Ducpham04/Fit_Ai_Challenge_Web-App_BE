package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.TrainingPlanDetail;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanDetailRepository extends JpaRepository<TrainingPlanDetail, Long> {
    List<TrainingPlanDetail> findByTrainingPlan_TpId(Long tpId);


}
