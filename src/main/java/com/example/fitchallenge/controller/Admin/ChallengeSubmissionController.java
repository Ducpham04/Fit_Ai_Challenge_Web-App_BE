package com.example.fitchallenge.controller.Admin;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.repository.UserChallengeRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin")
@RequiredArgsConstructor
public class ChallengeSubmissionController {

    private final UserChallengeRepository userChallengeRepository;

    /**
     * GET /api/admin/challenges/submissions
     * Lấy tất cả submissions
     */
    @GetMapping("/challenges/submissions")
    public ResponseEntity<NotificationResponse> getAllSubmissions(
            @RequestParam(required = false) Long challengeId,
            @RequestParam(required = false) String status) {
        
        if (challengeId != null) {
            var submissions = status != null ?
                    userChallengeRepository.findByChallenge_IdAndStatus(challengeId, status) :
                    userChallengeRepository.findByChallenge_Id(challengeId);
            return ResponseEntity.ok(new NotificationResponse(true, "Submissions retrieved", submissions));
        } else {
            var allSubmissions = userChallengeRepository.findAllWithUserAndChallenge();
            return ResponseEntity.ok(new NotificationResponse(true, "All submissions retrieved", allSubmissions));
        }
    }

    /**
     * GET /api/admin/challenges/{id}/submissions
     * Lấy submissions của challenge cụ thể
     */
    @GetMapping("/challenges/{id}/submissions")
    public ResponseEntity<NotificationResponse> getChallengeSubmissions(
            @PathVariable Long id,
            @RequestParam(required = false) String status) {
        
        var submissions = status != null ?
                userChallengeRepository.findByChallenge_IdAndStatus(id, status) :
                userChallengeRepository.findByChallenge_Id(id);
        
        return ResponseEntity.ok(new NotificationResponse(true, "Challenge submissions retrieved", submissions));
    }

}

