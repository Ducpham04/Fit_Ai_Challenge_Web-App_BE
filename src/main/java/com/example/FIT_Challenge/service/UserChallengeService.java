package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.UserChallengeDTO.UserChallengeDTO;
import com.example.FIT_Challenge.config.NotificationResponse;

import java.util.List;

public interface UserChallengeService {
    NotificationResponse getAll();
    NotificationResponse getById(Long id);
    NotificationResponse create(UserChallengeDTO dto);
    NotificationResponse update(Long id, UserChallengeDTO dto);
    NotificationResponse delete(Long id);
}
