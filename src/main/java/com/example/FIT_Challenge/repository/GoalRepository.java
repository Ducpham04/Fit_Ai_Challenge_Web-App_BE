package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.Goals;
import org.springframework.data.jpa.repository.JpaRepository;

public interface GoalRepository extends JpaRepository<Goals, Long> {


    boolean existsByName(String name);
}
