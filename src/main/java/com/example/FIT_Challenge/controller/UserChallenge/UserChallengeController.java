package com.example.FIT_Challenge.controller.UserChallenge;

import com.example.FIT_Challenge.DTO.UserChallengeDTO.UserChallengeDTO;
import com.example.FIT_Challenge.config.NotificationResponse;
import com.example.FIT_Challenge.service.UserChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/user-challenges")
@RequiredArgsConstructor
public class UserChallengeController {

    private final UserChallengeService userChallengeService;

    @GetMapping
    public ResponseEntity<NotificationResponse> getAll() {
        return ResponseEntity.ok(userChallengeService.getAll());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getById(@PathVariable Long id) {
        return ResponseEntity.ok(userChallengeService.getById(id));
    }

    @PostMapping
    public ResponseEntity<NotificationResponse> create(@RequestBody UserChallengeDTO dto) {
        return ResponseEntity.ok(userChallengeService.create(dto));
    }

    @PutMapping("/{id}")
    public ResponseEntity<NotificationResponse> update(@PathVariable Long id, @RequestBody UserChallengeDTO dto) {
        return ResponseEntity.ok(userChallengeService.update(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> delete(@PathVariable Long id) {
        return ResponseEntity.ok(userChallengeService.delete(id));
    }
}
