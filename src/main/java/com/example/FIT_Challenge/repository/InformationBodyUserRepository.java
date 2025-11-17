package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.Entity.InformationBodyUser;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface InformationBodyUserRepository extends JpaRepository<InformationBodyUser, Long> {
    List<InformationBodyUser> findByUserId(Long userId);
    
}
