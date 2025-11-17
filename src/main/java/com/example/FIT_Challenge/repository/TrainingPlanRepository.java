package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.TrainingPlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    List<TrainingPlan> findByGoalId(Long goalId);

}
