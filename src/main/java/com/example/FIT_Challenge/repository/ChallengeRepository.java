package com.example.FIT_Challenge.repository;


import com.example.FIT_Challenge.Entity.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenges, Long> {
    boolean existsByTitle(String title);
}
