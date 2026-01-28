package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.Meal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface MealRepository extends JpaRepository<Meal, Long> {
    List<Meal> findByNutritionPlanPlanId(Long planId);
}
