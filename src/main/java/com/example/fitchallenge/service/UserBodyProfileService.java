package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.UserBodyProfileDTO.UserBodyProfileRequest;
import com.example.fitchallenge.config.NotificationResponse;

public interface UserBodyProfileService {
    NotificationResponse createOrUpdateBodyProfile(Long userId, UserBodyProfileRequest request);
    NotificationResponse getBodyProfile(Long userId);
}


