package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.MealFood;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MealFoodRepository extends JpaRepository<MealFood, Long> {
    List<MealFood> findByMealMealId(Long id);
}
