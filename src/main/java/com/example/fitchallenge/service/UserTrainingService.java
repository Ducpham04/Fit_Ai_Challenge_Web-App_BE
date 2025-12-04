package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserTrainingService {
    NotificationResponse getUserTrainingDetails(Long userId);
    NotificationResponse createUserTraining(UserRequestDTO res);
}
