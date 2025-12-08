package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.HealthProfile;
import com.example.fitchallenge.Entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface HealthProfileRepository extends JpaRepository<HealthProfile, Long> {
    Optional<HealthProfile> findByUser(User user);
    Optional<HealthProfile> findByUser_Id(Long userId);
    boolean existsByUser_Id(Long userId);
}


