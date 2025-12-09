package com.example.fitchallenge.service;

import com.example.fitchallenge.DTO.ChallengeDTO.ChallengeDTOPayload;
import com.example.fitchallenge.DTO.ChallengeDTO.ChallengeResponseDTO;
import com.example.fitchallenge.config.NotificationResponse;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.web.multipart.MultipartFile;

public interface ChallengeService {
    NotificationResponse createChallenge(ChallengeDTOPayload dto, MultipartFile video);
    NotificationResponse getAllChallenges();
    NotificationResponse getChallengeById(Long id);
    NotificationResponse updateChallenge(Long id, ChallengeDTOPayload dto,  MultipartFile video);
    NotificationResponse deleteChallenge(Long id);
    
    // New methods for user-facing APIs
    Page<ChallengeResponseDTO> getAllChallengesForUser(String status, String difficulty, Pageable pageable);
    ChallengeResponseDTO getChallengeByIdForUser(Long id);
    NotificationResponse joinChallenge(Long challengeId, Long userId);
}
