package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.DTO.UserChallengeDTO.UserChallengeDTO;
import com.example.fitchallenge.Entity.Challenges;
import com.example.fitchallenge.Entity.User;
import com.example.fitchallenge.Entity.UserChallenge;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.ChallengeRepository;
import com.example.fitchallenge.repository.UserChallengeRepository;
import com.example.fitchallenge.repository.User.UserRepository;
import com.example.fitchallenge.service.UserChallengeService;


import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class UserChallengeImp implements UserChallengeService {

    private final UserChallengeRepository userChallengeRepository;
    private final UserRepository userRepository;
    private final ChallengeRepository challengeRepository;

    @Override
    @Transactional(readOnly = true)
    public NotificationResponse getAll() {
        List<UserChallengeDTO> list = userChallengeRepository.findAllWithUserAndChallenge()
                .stream()
                .map(uc -> new UserChallengeDTO(
                        uc.getUcId(),
                        uc.getUser().getId(),
                        uc.getChallenge().getId(),
                        uc.getStatus(),
                        uc.getVideoUrl(),
                        uc.getKeypointsPayload(),
                        uc.getScore(),
                        uc.getConfidence(),
                        uc.getSubmittedAt(),
                        uc.getCompletedAt()
                ))
                .collect(Collectors.toList());

        return new NotificationResponse(true, "Danh sách tất cả user challenge", list);
    }


    @Override
    public NotificationResponse getById(Long id) {
        Optional<UserChallenge> found = userChallengeRepository.findById(id);
        return found.map(userChallenge -> new NotificationResponse(true, "Tìm thấy bản ghi", userChallenge))
                .orElseGet(() -> new NotificationResponse(false, "Không tìm thấy user challenge có id = " + id));
    }

    @Override
    public NotificationResponse create(UserChallengeDTO dto) {
        try {
            User user = userRepository.findById(dto.getUserId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy user có id " + dto.getUserId()));

            Challenges challenge = challengeRepository.findById(dto.getChallengeId())
                    .orElseThrow(() -> new RuntimeException("Không tìm thấy challenge có id " + dto.getChallengeId()));

            UserChallenge newRecord = UserChallenge.builder()
                    .user(user)
                    .challenge(challenge)
                    .status(dto.getStatus())
                    .videoUrl(dto.getVideoUrl())
                    .keypointsPayload(dto.getKeypointsPayload())
                    .score(dto.getScore())
                    .confidence(dto.getConfidence())
                    .build();

            userChallengeRepository.save(newRecord);
            return new NotificationResponse(true, "Tạo user challenge thành công", newRecord);

        } catch (Exception e) {
            return new NotificationResponse(false, "Lỗi khi tạo user challenge: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse update(Long id, UserChallengeDTO dto) {
        Optional<UserChallenge> optional = userChallengeRepository.findById(id);

        if (optional.isEmpty()) {
            return new NotificationResponse(false, "Không tìm thấy bản ghi để cập nhật");
        }

        try {
            UserChallenge existing = optional.get();

            if (dto.getStatus() != null) existing.setStatus(dto.getStatus());
            if (dto.getVideoUrl() != null) existing.setVideoUrl(dto.getVideoUrl());
            if (dto.getKeypointsPayload() != null) existing.setKeypointsPayload(dto.getKeypointsPayload());
            if (dto.getScore() != null) existing.setScore(dto.getScore());
            if (dto.getConfidence() != null) existing.setConfidence(dto.getConfidence());

            userChallengeRepository.save(existing);
            return new NotificationResponse(true, "Cập nhật thành công", existing);

        } catch (Exception e) {
            return new NotificationResponse(false, "Lỗi khi cập nhật: " + e.getMessage());
        }
    }

    @Override
    public NotificationResponse delete(Long id) {
        if (!userChallengeRepository.existsById(id)) {
            return new NotificationResponse(false, "Không tìm thấy user challenge để xóa");
        }
        userChallengeRepository.deleteById(id);
        return new NotificationResponse(true, "Xóa user challenge thành công");
    }
}
