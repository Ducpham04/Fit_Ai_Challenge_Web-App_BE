package com.example.FIT_Challenge.service;



import com.example.FIT_Challenge.DTO.ChallengeDTO.ChallengeDTOPayload;

import com.example.FIT_Challenge.config.NotificationResponse;
import org.springframework.web.multipart.MultipartFile;

public interface ChallengeService {
    NotificationResponse createChallenge(ChallengeDTOPayload dto, MultipartFile video);
    NotificationResponse getAllChallenges();
    NotificationResponse getChallengeById(Long id);
    NotificationResponse updateChallenge(Long id, ChallengeDTOPayload dto,  MultipartFile video);
    NotificationResponse deleteChallenge(Long id);
}
