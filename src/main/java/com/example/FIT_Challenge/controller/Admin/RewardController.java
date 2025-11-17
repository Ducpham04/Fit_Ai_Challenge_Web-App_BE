package com.example.FIT_Challenge.controller.Admin;

import com.example.FIT_Challenge.DTO.RewardDTO.RewardRequest;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.RewardService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/rewards")
@RequiredArgsConstructor
public class RewardController {

    private final RewardService rewardService;

    @PostMapping
    public NotificationResponse create(@RequestBody RewardRequest request) {
        return rewardService.createReward(request);
    }

    @GetMapping
    public NotificationResponse getAll() {
        return rewardService.getAllRewards();
    }

    @GetMapping("/{id}")
    public NotificationResponse getById(@PathVariable Long id) {
        return rewardService.getRewardById(id);
    }

    @PutMapping("/{id}")
    public NotificationResponse update(@PathVariable Long id, @RequestBody RewardRequest request) {
        return rewardService.updateReward(id, request);
    }

    @DeleteMapping("/{id}")
    public NotificationResponse delete(@PathVariable Long id) {
        return rewardService.deleteReward(id);
    }
}
