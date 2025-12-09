package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.UserInfoDTO;
import com.example.fitchallenge.Entity.Goals;
import com.example.fitchallenge.Entity.InformationBodyUser;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.GoalRepository;
import com.example.fitchallenge.repository.InformationBodyUserRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.service.UserInfoService;
import com.example.fitchallenge.utils.BodyMetricsCalculator;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserInfoServiceImpl implements UserInfoService {

    private final InformationBodyUserRepository informationBodyUserRepository;
    private final UserRepository userRepository;
    private final GoalRepository goalRepository;

    @Override
    public NotificationResponse getUserInfo(Long userId) {
        try {
            // Lấy user
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new NotificationResponse(false, "User not found");
            }
            User user = userOpt.get();

            // Lấy body info mới nhất của user
            List<InformationBodyUser> bodyInfoList = informationBodyUserRepository.findByUserId(userId);
            InformationBodyUser bodyInfo = bodyInfoList.isEmpty() ? null : bodyInfoList.get(0);

            // Tạo DTO
            UserInfoDTO dto = new UserInfoDTO();
            dto.setUserId(user.getId());
            dto.setUserName(user.getUserName());
            dto.setEmail(user.getEmail());
            dto.setAvatar(user.getLinkImage());

            if (bodyInfo != null) {
                dto.setInfoId(bodyInfo.getInfoId());
                dto.setHeightCm(bodyInfo.getHeightCm());
                dto.setWeightKg(bodyInfo.getWeightKg());
                dto.setAge(bodyInfo.getAge());
                dto.setGender(bodyInfo.getGender());
                dto.setBodyFatPct(bodyInfo.getBodyFatPct());
                dto.setBmi(bodyInfo.getBmi());
                dto.setActivityLevel(bodyInfo.getActivityLevel());
                dto.setBmr(bodyInfo.getBmr());
                dto.setRecommendedCalories(bodyInfo.getRecommendedCalories());
                
                if (bodyInfo.getGoals() != null) {
                    dto.setGoalId(bodyInfo.getGoals().getId());
                    dto.setGoalName(bodyInfo.getGoals().getName());
                }
                
                dto.setCreatedAt(bodyInfo.getCreatedAt() != null ? bodyInfo.getCreatedAt().toString() : null);
            }

            return new NotificationResponse(true, "User info retrieved successfully", dto);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error retrieving user info: " + e.getMessage());
        }
    }

    @Override
    @Transactional
    public NotificationResponse updateUserInfo(Long userId, UserInfoDTO dto) {
        try {
            // Validate input
            if (dto == null) {
                return new NotificationResponse(false, "Request body cannot be null");
            }
            
            // Kiểm tra user tồn tại
            Optional<User> userOpt = userRepository.findById(userId);
            if (userOpt.isEmpty()) {
                return new NotificationResponse(false, "User not found");
            }
            User user = userOpt.get();

            // Lấy hoặc tạo body info
            List<InformationBodyUser> bodyInfoList = informationBodyUserRepository.findByUserId(userId);
            InformationBodyUser bodyInfo = bodyInfoList.isEmpty() ? new InformationBodyUser() : bodyInfoList.get(0);

            // Cập nhật thông tin
            if (bodyInfo.getInfoId() == null) {
                bodyInfo.setUser(user);
                bodyInfo.setCreatedAt(ZonedDateTime.now());
            }

            if (dto.getHeightCm() != null) bodyInfo.setHeightCm(dto.getHeightCm());
            if (dto.getWeightKg() != null) bodyInfo.setWeightKg(dto.getWeightKg());
            if (dto.getAge() != null) bodyInfo.setAge(dto.getAge());
            if (dto.getGender() != null) bodyInfo.setGender(dto.getGender());
            if (dto.getBodyFatPct() != null) bodyInfo.setBodyFatPct(dto.getBodyFatPct());
            if (dto.getActivityLevel() != null) bodyInfo.setActivityLevel(dto.getActivityLevel());

            // Cập nhật goal nếu có
            if (dto.getGoalId() != null) {
                Optional<Goals> goalOpt = goalRepository.findById(dto.getGoalId());
                if (goalOpt.isPresent()) {
                    bodyInfo.setGoals(goalOpt.get());
                }
            }

            // Tự động tính toán BMI, BMR, recommended calories
            if (bodyInfo.getWeightKg() != null && bodyInfo.getHeightCm() != null) {
                BigDecimal bmi = BodyMetricsCalculator.calculateBMI(
                    bodyInfo.getWeightKg(), 
                    bodyInfo.getHeightCm()
                );
                bodyInfo.setBmi(bmi);

                if (bodyInfo.getAge() != null && bodyInfo.getGender() != null) {
                    BigDecimal bmr = BodyMetricsCalculator.calculateBMR(
                        bodyInfo.getWeightKg(),
                        bodyInfo.getHeightCm(),
                        bodyInfo.getAge(),
                        bodyInfo.getGender()
                    );
                    bodyInfo.setBmr(bmr);

                    String activityLevel = bodyInfo.getActivityLevel() != null 
                        ? bodyInfo.getActivityLevel() 
                        : "sedentary"; // Mặc định
                    
                    BigDecimal recommendedCalories = BodyMetricsCalculator.calculateRecommendedCalories(
                        bmr, 
                        activityLevel
                    );
                    bodyInfo.setRecommendedCalories(recommendedCalories);
                }
            }

            // Lưu
            informationBodyUserRepository.save(bodyInfo);

            // Trả về DTO đã cập nhật
            UserInfoDTO responseDto = new UserInfoDTO();
            responseDto.setInfoId(bodyInfo.getInfoId());
            responseDto.setUserId(user.getId());
            responseDto.setUserName(user.getUserName());
            responseDto.setEmail(user.getEmail());
            responseDto.setAvatar(user.getLinkImage());
            responseDto.setHeightCm(bodyInfo.getHeightCm());
            responseDto.setWeightKg(bodyInfo.getWeightKg());
            responseDto.setAge(bodyInfo.getAge());
            responseDto.setGender(bodyInfo.getGender());
            responseDto.setBodyFatPct(bodyInfo.getBodyFatPct());
            responseDto.setBmi(bodyInfo.getBmi());
            responseDto.setActivityLevel(bodyInfo.getActivityLevel());
            responseDto.setBmr(bodyInfo.getBmr());
            responseDto.setRecommendedCalories(bodyInfo.getRecommendedCalories());
            
            if (bodyInfo.getGoals() != null) {
                responseDto.setGoalId(bodyInfo.getGoals().getId());
                responseDto.setGoalName(bodyInfo.getGoals().getName());
            }
            
            responseDto.setCreatedAt(bodyInfo.getCreatedAt().toString());

            return new NotificationResponse(true, "User info updated successfully", responseDto);
        } catch (Exception e) {
            return new NotificationResponse(false, "Error updating user info: " + e.getMessage());
        }
    }
}

