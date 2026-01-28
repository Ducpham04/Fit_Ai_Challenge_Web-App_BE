package com.example.fitchallenge.controller.admin;


import com.example.fitchallenge.dto.challengedto.ChallengePayloadDTO;

import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.ChallengeService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/admin/challenges")
@RequiredArgsConstructor
public class AdminChallengeController {

    private final ChallengeService challengeService;

    @PostMapping(consumes = {"multipart/form-data"})
    public ResponseEntity<NotificationResponse> createChallenge(
            @RequestPart (value= "data") String dtoJson,
            @RequestPart(value = "video", required = false) MultipartFile video) throws JsonProcessingException {



        ObjectMapper objectMapper = new ObjectMapper();

        ChallengePayloadDTO dto = objectMapper.readValue(dtoJson,ChallengePayloadDTO.class) ;



        return ResponseEntity.ok(challengeService.createChallenge(dto, video));
    }


    @GetMapping
    public ResponseEntity<NotificationResponse> getAllChallenges() {
        return ResponseEntity.ok(challengeService.getAllChallenges());
    }

    @GetMapping("/{id}")
    public ResponseEntity<NotificationResponse> getChallengeById(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.getChallengeById(id));
    }

    @PutMapping(value = "/{id}", consumes = {"multipart/form-data"})
    public ResponseEntity<NotificationResponse> updateChallenge(
            @PathVariable Long id,
            @RequestPart("data") String dtoJson,

            @RequestPart(value = "video", required = false) MultipartFile video) throws JsonProcessingException {
        ObjectMapper objectMapper = new ObjectMapper();
        ChallengePayloadDTO dto = objectMapper.readValue(dtoJson,ChallengePayloadDTO.class) ;
        return ResponseEntity.ok(challengeService.updateChallenge(id, dto, video));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deleteChallenge(@PathVariable Long id) {
        return ResponseEntity.ok(challengeService.deleteChallenge(id));
    }
}
