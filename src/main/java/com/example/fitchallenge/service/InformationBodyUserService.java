package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.InformationBodyDTO;
import com.example.fitchallenge.config.NotificationResponse;

public interface InformationBodyUserService  {
    NotificationResponse findByUserId(Long userId);
    NotificationResponse createInformationBodyUser(InformationBodyDTO informationBodyDTO);
    NotificationResponse updateInformationBodyUser(InformationBodyDTO informationBodyDTO);
    NotificationResponse deleteInformationBodyUserById(Long id);
    NotificationResponse getAllInformationBodyUsers();
}
