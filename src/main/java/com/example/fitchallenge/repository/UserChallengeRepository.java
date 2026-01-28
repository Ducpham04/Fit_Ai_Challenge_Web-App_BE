package com.example.fitchallenge.repository;

import com.example.fitchallenge.entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    @Query("SELECT uc FROM UserChallenge uc JOIN FETCH uc.user JOIN FETCH uc.challenge")
    List<UserChallenge> findAllWithUserAndChallenge();
    
    // Query methods for user profile and challenge participants
    List<UserChallenge> findByUser_Id(Long userId);
    List<UserChallenge> findByUser_IdAndStatus(Long userId, String status);
    List<UserChallenge> findByChallenge_Id(Long challengeId);
    List<UserChallenge> findByChallenge_IdAndStatus(Long challengeId, String status);
    long countByChallenge_Id(Long challengeId);
    long countByChallenge_IdAndStatus(Long challengeId, String status);
}
