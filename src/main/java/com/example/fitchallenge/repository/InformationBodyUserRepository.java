package com.example.fitchallenge.repository;

import com.example.fitchallenge.Entity.InformationBodyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationBodyUserRepository extends JpaRepository<InformationBodyUser, Long> {
    List<InformationBodyUser> findByUserId(Long userId);
    
}
