package com.example.FIT_Challenge.service;

import com.example.FIT_Challenge.DTO.goalsDTO.goalsDTOpayload;
import com.example.FIT_Challenge.config.NotificationResponse;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
@Service
public interface GoalService {

    List<goalsDTOpayload> getGoals() ;
    NotificationResponse createGoal(goalsDTOpayload goalsDTOpayload , MultipartFile image) ;
    NotificationResponse delete(Long id) ;
    NotificationResponse update(Long id, goalsDTOpayload goalsDTOpayload) ;

}
