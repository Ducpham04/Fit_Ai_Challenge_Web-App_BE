package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.UserTrainingDTO.UserRequestDTO;
import com.example.FIT_Challenge.config.NotificationResponse;
import org.springframework.stereotype.Service;

@Service
public interface UserTrainingService {
    NotificationResponse getUserTrainingDetails(Long userId);
    NotificationResponse createUserTraining(UserRequestDTO res);
}
