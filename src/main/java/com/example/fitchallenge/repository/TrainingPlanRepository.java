package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.TrainingPlan;

import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TrainingPlanRepository extends JpaRepository<TrainingPlan, Long> {
    List<TrainingPlan> findByGoalId(Long goalId);

}
