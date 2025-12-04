package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.ChallengeDTO.ChallengeDTOPayload;
import com.example.fitchallenge.DTO.TrainingPlanDetailDTO.TrainingPlanDetailRequest;
import com.example.fitchallenge.DTO.TrainingPlanDetailDTO.TrainingPlanDetailResponse;
import com.example.fitchallenge.Entity.Challenges;
import com.example.fitchallenge.Entity.TrainingPlan;
import com.example.fitchallenge.Entity.TrainingPlanDetail;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.ChallengeRepository;
import com.example.fitchallenge.repository.TrainingPlanDetailRepository;
import com.example.fitchallenge.repository.TrainingPlanRepository;
import com.example.fitchallenge.service.TrainingPlanDetailService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class TrainingPlanDetailImpl implements TrainingPlanDetailService {

    private final TrainingPlanDetailRepository trainingPlanDetailRepository;
    private final TrainingPlanRepository trainingPlanRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    public NotificationResponse createDetail(TrainingPlanDetailRequest dto) {
        try {
            Optional<TrainingPlan> planOpt = trainingPlanRepository.findById(dto.getTrainingPlanId());



            if (planOpt.isEmpty()) return new NotificationResponse(false, "Training plan not found");

            Optional<Challenges> challengeOpt = challengeRepository.findById(dto.getChallengeId());
            if (challengeOpt.isEmpty()) return new NotificationResponse(false, "Challenge not found");

            TrainingPlanDetail detail = new TrainingPlanDetail();
            detail.setTrainingPlan(planOpt.get());
            detail.setChallenge(challengeOpt.get());
            detail.setDayNumber(dto.getDayNumber());
            detail.setSets(dto.getSets());
            detail.setReps(dto.getReps());

            trainingPlanDetailRepository.save(detail);
            return new NotificationResponse(true, "Detail created successfully", toResponse(detail));

        } catch (Exception e) {
            return new NotificationResponse(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse updateDetail(Long id, TrainingPlanDetailRequest dto) {
        try {
            Optional<TrainingPlanDetail> detailOpt = trainingPlanDetailRepository.findById(id);
            if (detailOpt.isEmpty()) return new NotificationResponse(false, "Detail not found");

            TrainingPlanDetail detail = detailOpt.get();

            if (dto.getDayNumber() != null) detail.setDayNumber(dto.getDayNumber());
            if (dto.getSets() != null) detail.setSets(dto.getSets());
            if (dto.getReps() != null) detail.setReps(dto.getReps());

            if (dto.getChallengeId() != null) {
                challengeRepository.findById(dto.getChallengeId()).ifPresent(detail::setChallenge);
            }

            trainingPlanDetailRepository.save(detail);
            return new NotificationResponse(true, "Detail updated successfully", toResponse(detail));

        } catch (Exception e) {
            return new NotificationResponse(false, "Error: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse deleteDetail(Long id) {
        if (!trainingPlanDetailRepository.existsById(id)) {
            return new NotificationResponse(false, "Detail not found");
        }
        trainingPlanDetailRepository.deleteById(id);
        return new NotificationResponse(true, "Detail deleted successfully");
    }

    @Override
    public NotificationResponse getAllDetails() {
        List<TrainingPlanDetailResponse> details = trainingPlanDetailRepository.findAll()
                .stream().map(this::toResponse).toList();
        return new NotificationResponse(true, "All details", details);
    }

    @Override
    public NotificationResponse getDetailsByPlanId(Long planId) {
        List<TrainingPlanDetailResponse> details = trainingPlanDetailRepository.findByTrainingPlan_TpId(planId)
                .stream().map(this::toResponse).toList();
        return new NotificationResponse(true, "Details by plan id", details);
    }

    private TrainingPlanDetailResponse toResponse(TrainingPlanDetail detail) {

        ChallengeDTOPayload challengeDTO = new ChallengeDTOPayload(
                detail.getChallenge().getId(),
                detail.getChallenge().getTitle(),
                detail.getChallenge().getDescription(),
                detail.getChallenge().getDifficult().toString(),
                detail.getChallenge().getLinkVideos(),
                detail.getChallenge().getStatus().toString()
        );

        TrainingPlanDetailResponse dto = new TrainingPlanDetailResponse();
        dto.setTpdId(detail.getTpdId());
        dto.setTrainingPlanId(detail.getTrainingPlan().getTpId());
        dto.setTrainingPlanTitle(detail.getTrainingPlan().getTitle());
        dto.setDayNumber(detail.getDayNumber());
        dto.setChallenge(challengeDTO);  // gán payload chuẩn
        dto.setChallengeName(detail.getChallenge().getTitle());
        dto.setSets(detail.getSets());
        dto.setReps(detail.getReps());

        return dto;
    }

}
