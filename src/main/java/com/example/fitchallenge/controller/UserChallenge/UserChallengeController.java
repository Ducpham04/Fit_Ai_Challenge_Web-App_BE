package com.example.fitchallenge.controller.UserChallenge;

import com.example.fitchallenge.DTO.UserChallengeDTO.UserChallengeDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserChallengeService;
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

    /**
     * PUT /api/admin/user-challenges/{id}/feedback
     * Gửi feedback thủ công (cần thêm field adminFeedback vào entity)
     */
    @PutMapping("/{id}/feedback")
    public ResponseEntity<NotificationResponse> addFeedback(
            @PathVariable Long id,
            @RequestBody FeedbackRequest request) {
        
        // TODO: Implement khi có field adminFeedback trong UserChallenge entity
        // Hiện tại có thể update score nếu cần
        if (request.getAdjustedScore() != null) {
            UserChallengeDTO dto = new UserChallengeDTO();
            dto.setUcId(id);
            dto.setScore(request.getAdjustedScore());
            return ResponseEntity.ok(userChallengeService.update(id, dto));
        }
        
        return ResponseEntity.ok(new NotificationResponse(false, 
                "Feature not fully implemented yet. Need to add adminFeedback field to UserChallenge entity"));
    }

    @lombok.Data
    @lombok.NoArgsConstructor
    @lombok.AllArgsConstructor
    public static class FeedbackRequest {
        private String feedback;
        private Integer adjustedScore; // Điều chỉnh score nếu cần
    }
}
