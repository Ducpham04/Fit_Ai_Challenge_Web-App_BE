package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByNutritionPlanPlanId(Long planId);
}
