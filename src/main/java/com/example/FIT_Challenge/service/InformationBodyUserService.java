package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.InformationBodyDTO;
import com.example.FIT_Challenge.config.NotificationResponse;

public interface InformationBodyUserService  {
    NotificationResponse findByUserId(Long userId);
    NotificationResponse createInformationBodyUser(InformationBodyDTO informationBodyDTO);
    NotificationResponse updateInformationBodyUser(InformationBodyDTO informationBodyDTO);
    NotificationResponse deleteInformationBodyUserById(Long id);
    NotificationResponse getAllInformationBodyUsers();
}
