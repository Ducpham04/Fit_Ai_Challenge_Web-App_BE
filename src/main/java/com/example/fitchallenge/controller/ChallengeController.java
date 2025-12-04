package com.example.fitchallenge.controller;

import com.example.fitchallenge.DTO.ChallengeDTO.ChallengeResponseDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.ChallengeService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller cho Challenge APIs dành cho user (non-admin)
 */
@RestController
@RequestMapping("/api/challenges")
@RequiredArgsConstructor
public class ChallengeController {

    private final ChallengeService challengeService;

    /**
     * Get all challenges với pagination và filters
     * 
     * @param status Filter by status (Active, Upcoming, Completed)
     * @param difficulty Filter by difficulty (Easy, Medium, Hard)
     * @param page Page number (default: 0)
     * @param limit Items per page (default: 10)
     * @return Page of ChallengeResponseDTO
     */
    @GetMapping
    public ResponseEntity<Page<ChallengeResponseDTO>> getAllChallenges(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String difficulty,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<ChallengeResponseDTO> challenges = challengeService.getAllChallengesForUser(status, difficulty, pageable);
        
        return ResponseEntity.ok(challenges);
    }

    /**
     * Get challenge by ID với participants list
     * 
     * @param id Challenge ID
     * @return ChallengeResponseDTO với participants list
     */
    @GetMapping("/{id}")
    public ResponseEntity<ChallengeResponseDTO> getChallengeById(@PathVariable Long id) {
        ChallengeResponseDTO challenge = challengeService.getChallengeByIdForUser(id);
        return ResponseEntity.ok(challenge);
    }

    /**
     * Join a challenge
     * 
     * @param id Challenge ID
     * @param request Body với userId
     * @return NotificationResponse
     */
    @PostMapping("/{id}/join")
    public ResponseEntity<NotificationResponse> joinChallenge(
            @PathVariable Long id,
            @RequestBody JoinChallengeRequest request) {
        
        NotificationResponse response = challengeService.joinChallenge(id, request.getUserId());
        return ResponseEntity.ok(response);
    }
    
    // Inner class for join request
    @lombok.Data
    @lombok.AllArgsConstructor
    @lombok.NoArgsConstructor
    public static class JoinChallengeRequest {
        private Long userId;
    }
}

