package com.example.FIT_Challenge.service.impl;

import com.example.FIT_Challenge.DTO.InformationBodyDTO;
import com.example.FIT_Challenge.Entity.Goals;
import com.example.FIT_Challenge.Entity.InformationBodyUser;
import com.example.FIT_Challenge.Entity.User;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.repository.GoalRepository;
import com.example.FIT_Challenge.repository.InformationBodyUserRepository;
import com.example.FIT_Challenge.repository.User.UserRepository;
import com.example.FIT_Challenge.service.InformationBodyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class InformationBodyImpl implements InformationBodyUserService {

    private final InformationBodyUserRepository informationBodyUserRepository;
    private final UserRepository usersRepository;
    private final GoalRepository goalsRepository;

    @Override
    public NotificationResponse findByUserId(Long userId) {
        List<InformationBodyUser> infUser = informationBodyUserRepository.findByUserId(userId);
        List<InformationBodyDTO> dtoList = infUser.stream().map(this::toDto).toList();
        return new NotificationResponse(true, "Success", dtoList);
    }

    @Override
    public NotificationResponse createInformationBodyUser(InformationBodyDTO dto) {
        try {
            Optional<User> userOpt = usersRepository.findById(dto.getUserId());
            if (userOpt.isEmpty()) {
                return new NotificationResponse(false, "User not found");
            }

            Optional<Goals> goalOpt = goalsRepository.findById(dto.getGoalId());
            if (goalOpt.isEmpty()) {
                return new NotificationResponse(false, "Goal not found");
            }

            InformationBodyUser info = new InformationBodyUser();
            info.setUser(userOpt.get());
            info.setGoals(goalOpt.get());
            info.setHeightCm(dto.getHeightCm());
            info.setWeightKg(dto.getWeightKg());
            info.setAge(dto.getAge());
            info.setGender(dto.getGender());
            info.setBodyFatPct(dto.getBodyFatPct());
            info.setBmi(dto.getBmi());
            info.setCreatedAt(ZonedDateTime.now());

            informationBodyUserRepository.save(info);
            return new NotificationResponse(true, "Information body created successfully", toDto(info));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error creating record: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse updateInformationBodyUser(InformationBodyDTO dto) {
        try {
            Optional<InformationBodyUser> existingOpt = informationBodyUserRepository.findById(dto.getInfoId());
            if (existingOpt.isEmpty()) {
                return new NotificationResponse(false, "Information record not found");
            }

            InformationBodyUser info = existingOpt.get();

            if (dto.getHeightCm() != null) info.setHeightCm(dto.getHeightCm());
            if (dto.getWeightKg() != null) info.setWeightKg(dto.getWeightKg());
            if (dto.getAge() != null) info.setAge(dto.getAge());
            if (dto.getGender() != null) info.setGender(dto.getGender());
            if (dto.getBodyFatPct() != null) info.setBodyFatPct(dto.getBodyFatPct());
            if (dto.getBmi() != null) info.setBmi(dto.getBmi());

            if (dto.getGoalId() != null) {
                goalsRepository.findById(dto.getGoalId()).ifPresent(info::setGoals);
            }

            informationBodyUserRepository.save(info);
            return new NotificationResponse(true, "Information body updated successfully", toDto(info));
        } catch (Exception e) {
            return new NotificationResponse(false, "Error updating record: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse deleteInformationBodyUserById(Long id) {
        try {
            if (!informationBodyUserRepository.existsById(id)) {
                return new NotificationResponse(false, "Information record not found");
            }
            informationBodyUserRepository.deleteById(id);
            return new NotificationResponse(true, "Information record deleted successfully");
        } catch (Exception e) {
            return new NotificationResponse(false, "Error deleting record: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse getAllInformationBodyUsers() {
        List<InformationBodyUser> list = informationBodyUserRepository.findAll();
        List<InformationBodyDTO> dtoList = list.stream().map(this::toDto).toList();
        return new NotificationResponse(true, "All Information Body Users", dtoList);
    }

    private InformationBodyDTO toDto(InformationBodyUser info) {
        InformationBodyDTO dto = new InformationBodyDTO();
        dto.setInfoId(info.getInfoId());
        dto.setUserId(info.getUser().getId());
        dto.setUserName(info.getUser().getUserName());
        dto.setHeightCm(info.getHeightCm());
        dto.setWeightKg(info.getWeightKg());
        dto.setAge(info.getAge());
        dto.setGender(info.getGender());
        dto.setBodyFatPct(info.getBodyFatPct());
        dto.setBmi(info.getBmi());
        dto.setGoalId(info.getGoals().getId());
        dto.setGoalName(info.getGoals().getName());
        dto.setCreatedAt(info.getCreatedAt().toString());
        return dto;
    }
}
