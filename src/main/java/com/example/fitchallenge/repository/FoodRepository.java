package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.Food;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FoodRepository extends JpaRepository<Food,Long> {
}
