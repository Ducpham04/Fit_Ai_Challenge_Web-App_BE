package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.UserBodyProfileDTO.UserBodyProfileRequest;
import com.example.fitchallenge.DTO.UserBodyProfileDTO.UserBodyProfileResponse;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.Entity.UserBodyProfile;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.repository.UserBodyProfileRepository;
import com.example.fitchallenge.service.UserBodyProfileService;
import com.example.fitchallenge.utils.BodyMetricsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserBodyProfileServiceImpl implements UserBodyProfileService {

    private final UserBodyProfileRepository userBodyProfileRepository;
    private final UserRepository userRepository;

    @Override
    public NotificationResponse createOrUpdateBodyProfile(Long userId, UserBodyProfileRequest request) {
        try {
            User user = userRepository.findById(userId)
                    .orElseThrow(() -> new RuntimeException("User not found"));

            Optional<UserBodyProfile> existingProfile = userBodyProfileRepository.findByUser_Id(userId);
            UserBodyProfile profile;

            if (existingProfile.isPresent()) {
                profile = existingProfile.get();
            } else {
                profile = new UserBodyProfile();
                profile.setUser(user);
            }

            // Update fields
            profile.setHeight(request.getHeight());
            profile.setWeight(request.getWeight());
            profile.setBodyFat(request.getBodyFat());
            profile.setMuscleMass(request.getMuscleMass());
            profile.setAge(request.getAge());
            profile.setGender(request.getGender());
            profile.setExperienceLevel(request.getExperienceLevel());
            profile.setGoal(request.getGoal());
            profile.setInjuryNotes(request.getInjuryNotes());

            // Calculate BMI
            if (request.getHeight() != null && request.getWeight() != null) {
                BigDecimal heightCm = BigDecimal.valueOf(request.getHeight());
                BigDecimal weightKg = BigDecimal.valueOf(request.getWeight());
                BigDecimal bmi = BodyMetricsCalculator.calculateBMI(weightKg, heightCm);
                profile.setBmi(bmi.doubleValue());
            }

            UserBodyProfile saved = userBodyProfileRepository.save(profile);
            UserBodyProfileResponse response = toResponse(saved);

            return new NotificationResponse(true, "Body profile saved successfully", response);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error saving body profile: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getBodyProfile(Long userId) {
        try {
            Optional<UserBodyProfile> profile = userBodyProfileRepository.findByUser_Id(userId);
            if (profile.isEmpty()) {
                return new NotificationResponse(false, "Body profile not found");
            }
            UserBodyProfileResponse response = toResponse(profile.get());
            return new NotificationResponse(true, "Body profile retrieved successfully", response);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving body profile: " + e.getMessage());
        }
    }

    private UserBodyProfileResponse toResponse(UserBodyProfile profile) {
        UserBodyProfileResponse response = new UserBodyProfileResponse();
        response.setId(profile.getId());
        response.setUserId(profile.getUser().getId());
        response.setHeight(profile.getHeight());
        response.setWeight(profile.getWeight());
        response.setBmi(profile.getBmi());
        response.setBodyFat(profile.getBodyFat());
        response.setMuscleMass(profile.getMuscleMass());
        response.setAge(profile.getAge());
        response.setGender(profile.getGender());
        response.setExperienceLevel(profile.getExperienceLevel());
        response.setGoal(profile.getGoal());
        response.setInjuryNotes(profile.getInjuryNotes());
        response.setCreatedAt(profile.getCreatedAt());
        response.setUpdatedAt(profile.getUpdatedAt());
        return response;
    }
}

