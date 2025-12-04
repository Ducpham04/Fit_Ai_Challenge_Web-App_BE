package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.UserChallenge;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface UserChallengeRepository extends JpaRepository<UserChallenge, Long> {
    @Query("SELECT uc FROM UserChallenge uc JOIN FETCH uc.user JOIN FETCH uc.challenge")
    List<UserChallenge> findAllWithUserAndChallenge();
}
