package com.example.fitchallenge.repository;

import com.example.fitchallenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.fitchallenge.Entity.UserTraining;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserTrainingRepository extends JpaRepository<UserTraining, Long> {
    List<UserTraining> findUserTrainingDetailsByUserId(Long userId);

    void save(UserRequestDTO userRequestDTO);

    boolean existsByUser_IdAndTrainingPlan_TpId(Long userID, Long trainingID);
}
