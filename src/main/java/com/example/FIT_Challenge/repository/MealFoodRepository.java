package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
    List<MealFood> findByMealMealId(Long id);
}
