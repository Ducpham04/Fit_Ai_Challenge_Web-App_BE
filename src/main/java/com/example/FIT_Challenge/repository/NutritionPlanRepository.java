package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.NutritionPlan;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Collection;
import java.util.List;

public interface NutritionPlanRepository extends JpaRepository<NutritionPlan, Long> {
    List<NutritionPlan> findByGoalId(Long Goals) ;

    Collection<Object> findByGoal_Id(Long goalId);
}
