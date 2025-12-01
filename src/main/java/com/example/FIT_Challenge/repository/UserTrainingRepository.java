package com.example.FIT_Challenge.repository;

import com.example.FIT_Challenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.FIT_Challenge.Entity.UserTraining;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTrainingRepository extends JpaRepository<UserTraining, Long> {
    List<UserTraining> findUserTrainingDetailsByUserId(Long userId);

    void save(UserRequestDTO userRequestDTO);

    boolean existsByUser_IdAndTrainingPlan_TpId(Long userID, Long trainingID);
}
