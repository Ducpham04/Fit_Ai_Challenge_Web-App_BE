package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.UserNutrition;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserNutritionRepository extends JpaRepository<UserNutrition, Long> {
    List<UserNutrition> findByUser_Id(Long userId);
    List<UserNutrition> findByUser_IdAndStatus(Long userId, String status);
}
