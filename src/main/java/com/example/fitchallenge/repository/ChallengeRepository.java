package com.example.fitchallenge.repository;


import com.example.fitchallenge.entity.Challenges;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ChallengeRepository extends JpaRepository<Challenges, Long> {
    boolean existsByTitle(String title);
}
