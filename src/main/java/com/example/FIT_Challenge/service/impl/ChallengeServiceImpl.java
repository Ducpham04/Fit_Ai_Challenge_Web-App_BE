package com.example.FIT_Challenge.service.impl;


import com.example.FIT_Challenge.DTO.ChallengeDTO.ChallengeDTOPayload;

import com.example.FIT_Challenge.Entity.Challenges;
import com.example.FIT_Challenge.Entity.Goals;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.ChallengeRepository;
import com.example.FIT_Challenge.repository.GoalRepository;
import com.example.FIT_Challenge.service.ChallengeService;
import com.example.FIT_Challenge.service.FileStorageService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ChallengeServiceImpl implements ChallengeService {

    private final ChallengeRepository challengeRepository;
    private final GoalRepository goalRepository;
    private final FileStorageService fileStorageService;

    @Override
    public NotificationResponse createChallenge(ChallengeDTOPayload dto, MultipartFile video) {
        try {
            if (dto.getTitle() == null || dto.getTitle().trim().isEmpty()) {
                return new NotificationResponse(false, "Challenge title cannot be empty");
            }

            if (challengeRepository.existsByTitle(dto.getTitle())) {
                return new NotificationResponse(false, "Challenge title already exists");
            }




            Challenges challenge = new Challenges();

            challenge.setTitle(dto.getTitle().trim());
            challenge.setDescription(dto.getDescription());
            challenge.setDifficult(Challenges.DifficultLevel.valueOf(dto.getDifficult()));
            challenge.setStatus(Challenges.Status.valueOf(dto.getStatus()));

//            if (image != null && !image.isEmpty()) {
//                String imagePath = fileStorageService.uploadFile(image);
//                challenge.setLinkImage(imagePath);
//            }

            if (video != null && !video.isEmpty()) {
                String videoPath = fileStorageService.uploadFile(video);
                challenge.setLinkVideos(videoPath);
            }

            challengeRepository.save(challenge);
            return new NotificationResponse(true, "Challenge created successfully", challenge);

        } catch (Exception e) {
            return new NotificationResponse(false, "Error creating challenge: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getAllChallenges() {
        List<Challenges> list = challengeRepository.findAll();
        if (list.isEmpty()) {
            return new NotificationResponse(false, "No challenges found");
        }
        return new NotificationResponse(true, "Challenges retrieved successfully", list);
    }

    @Override
    public NotificationResponse getChallengeById(Long id) {
        return challengeRepository.findById(id)
                .map(ch -> new NotificationResponse(true, "Challenge found", ch))
                .orElseGet(() -> new NotificationResponse(false, "Challenge not found"));
    }

    @Override
    public NotificationResponse updateChallenge(Long id, ChallengeDTOPayload dto,  MultipartFile video) {
        try {
            Challenges challenge = challengeRepository.findById(id)
                    .orElseThrow(() -> new RuntimeException("Challenge not found"));

            if (dto.getTitle() != null) challenge.setTitle(dto.getTitle());
            if (dto.getDescription() != null) challenge.setDescription(dto.getDescription());
            if (dto.getDifficult() != null)
                challenge.setDifficult(Challenges.DifficultLevel.valueOf(dto.getDifficult()));
            if (dto.getStatus() != null)
                challenge.setStatus(Challenges.Status.valueOf(dto.getStatus()));

//            if (image != null && !image.isEmpty()) {
//                String imagePath = fileStorageService.uploadFile(image);
//                challenge.setLinkImage(imagePath);
//            }

            if (video != null && !video.isEmpty()) {
                String videoPath = fileStorageService.uploadFile(video);
                challenge.setLinkVideos(videoPath);
            }

            challengeRepository.save(challenge);
            return new NotificationResponse(true, "Challenge updated successfully", challenge);

        } catch (Exception e) {
            return new NotificationResponse(false, "Error updating challenge: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse deleteChallenge(Long id) {
        try {
            if (!challengeRepository.existsById(id)) {
                return new NotificationResponse(false, "Challenge not found");
            }
            challengeRepository.deleteById(id);
            return new NotificationResponse(true, "Challenge deleted successfully");
        } catch (Exception e) {
            return new NotificationResponse(false, "Error deleting challenge: " + e.getMessage());
        }
    }
}
