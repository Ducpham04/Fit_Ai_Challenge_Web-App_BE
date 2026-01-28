package com.example.fitchallenge.service.impl;

import com.example.fitchallenge.dto.rewardto.AdminRewardDTO;
import com.example.fitchallenge.entity.Reward;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.RewardRepository;
import com.example.fitchallenge.service.FileStorageService;
import com.example.fitchallenge.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.stream.Collectors;
@Service
@RequiredArgsConstructor
public class RewardServiceImpl implements RewardService {

    private final RewardRepository rewardRepository;
    private final FileStorageService fileService;

    @Override
    public NotificationResponse createReward(AdminRewardDTO request, MultipartFile file) {
        System.out.println("Creating reward with data: " + request.getName());
        Reward reward = Reward.builder()
                .name(request.getName())
                .description(request.getDescription())
                .costPoints(request.getPoints())
                .expireAt(request.getExpireAt())
                .claimed(request.getClaimed())
                .stock(request.getTotal())
                .externalPartner(request.getExternalPartner())
                .build();

        if (file != null && !file.isEmpty()) {
            String fileUrl = fileService.uploadFile(file);
            reward.setLinkImage(fileUrl); // sửa typo
        }

        rewardRepository.save(reward);
        return new NotificationResponse(true, "Reward created successfully", reward);
    }

    @Override
    public NotificationResponse updateReward(Long id, AdminRewardDTO request, MultipartFile file) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));

        reward.setName(request.getName());
        reward.setDescription(request.getDescription());
        reward.setCostPoints(request.getPoints());
        reward.setStock(request.getTotal());
        reward.setExpireAt(request.getExpireAt());
        reward.setClaimed(request.getClaimed());
        reward.setExternalPartner(request.getExternalPartner());

        // Upload file mới nếu có
        if (file != null && !file.isEmpty()) {
            String fileUrl = fileService.uploadFile(file);
            reward.setLinkImage(fileUrl);
        } else if (request.getLinkImage() != null) {
            // giữ link hiện tại nếu DTO gửi
            reward.setLinkImage(request.getLinkImage());
        }

        rewardRepository.save(reward);
        return new NotificationResponse(true, "Reward updated", reward);
    }

    @Override
    public NotificationResponse deleteReward(Long id) {
        rewardRepository.deleteById(id);
        return new NotificationResponse(true, "Reward deleted");
    }

    @Override
    public NotificationResponse getRewardById(Long id) {
        Reward reward = rewardRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Reward not found"));
        return new NotificationResponse(true, "Success", reward);
    }

    @Override
    public NotificationResponse getAllRewards() {
        List<AdminRewardDTO> rewards = rewardRepository.findAll().stream()
                .map(reward -> AdminRewardDTO.builder()
                        .id(reward.getRewardId())
                        .name(reward.getName())
                        .description(reward.getDescription())
                        .linkImage(reward.getLinkImage()) // sửa typo
                        .points(reward.getCostPoints())
                        .total(reward.getStock())
                        .claimed(reward.getClaimed())
                        .status(reward.getStock() > 0 ? "Available" : "Out of Stock")
                        .externalPartner(reward.getExternalPartner())
                        .expireAt(reward.getExpireAt())
                        .build())
                .collect(Collectors.toList());
        return new NotificationResponse(true, "Success", rewards);
    }
}
