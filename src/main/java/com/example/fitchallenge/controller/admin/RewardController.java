package com.example.fitchallenge.controller.admin;

import com.example.fitchallenge.dto.rewardto.AdminRewardDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.RewardService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("api/admin/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;
    private final ObjectMapper objectMapper; // Spring Boot auto config

    @PostMapping
    public NotificationResponse create(
            @RequestPart("reward") String rewardJson,
            @RequestPart(value = "file", required = false) MultipartFile file) throws JsonProcessingException {
        if (file != null && !file.isEmpty()) {
            String fileName = file.getOriginalFilename();
            System.out.println("Received file: " + fileName);
        }
        // Chuyển JSON string thành DTO
        AdminRewardDTO request = objectMapper.readValue(rewardJson, AdminRewardDTO.class);

        return rewardService.createReward(request, file);
    }

    @PutMapping("/{id}")
    public NotificationResponse update(
            @PathVariable Long id,
            @RequestParam("reward") String rewardJson,
            @RequestParam(value = "file", required = false) MultipartFile file) throws JsonProcessingException {

        AdminRewardDTO request = objectMapper.readValue(rewardJson, AdminRewardDTO.class);
        return rewardService.updateReward(id, request, file);
    }

    @GetMapping
    public NotificationResponse getAll() {
        return rewardService.getAllRewards();
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable Long id) {
        return rewardService.getRewardById(id);
    }

    @DeleteMapping("/{id}")
    public NotificationResponse delete(@PathVariable Long id) {
        return rewardService.deleteReward(id);
    }
}
