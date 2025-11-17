package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food,Long> {
}
