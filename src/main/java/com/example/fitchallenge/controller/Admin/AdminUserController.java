package com.example.fitchallenge.controller.Admin;

import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.UserDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserService;
import com.example.fitchallenge.service.PersonalizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

/**
 * Controller cho Admin User Management APIs
 */
@RestController
@RequestMapping("/api/admin/users")
@RequiredArgsConstructor
public class AdminUserController {

    private final UserService userService;
    private final com.example.fitchallenge.service.BodyMetricHistoryService bodyMetricHistoryService;
    private final com.example.fitchallenge.service.UserInfoService userInfoService;
    private final com.example.fitchallenge.service.UserTrainingService userTrainingService;
    private final PersonalizationService personalizationService;

    /**
     * Get all users với pagination và filters
     * 
     * @param status Filter by status (active, inactive, banned)
     * @param role Filter by role
     * @param page Page number (default: 0)
     * @param limit Items per page (default: 10)
     * @return Page of UserDTO
     */
    @GetMapping
    public ResponseEntity<Page<UserDTO>> getAllUsers(
            @RequestParam(required = false) String status,
            @RequestParam(required = false) String role,
            @RequestParam(required = false) String search,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserDTO> users = userService.getAllUsersPaginated(status, role, search, pageable);
        
        return ResponseEntity.ok(users);
    }

    /**
     * Create a new user
     * 
     * @param request RegisterRequestAdmin với user details
     * @return Created UserDTO
     */
    @PostMapping
    public ResponseEntity<UserDTO> createUser(@RequestBody RegisterRequestAdmin request) {
        UserDTO user = userService.createUser(request);
        return ResponseEntity.ok(user);
    }

    /**
     * Update user
     * 
     * @param id User ID
     * @param request RegisterRequestAdmin với updated fields
     * @return Updated UserDTO
     */
    @PutMapping("/{id}")
    public ResponseEntity<UserDTO> updateUser(
            @PathVariable Long id,
            @RequestBody RegisterRequestAdmin request) {
        UserDTO user = userService.updateUser(id, request);
        return ResponseEntity.ok(user);
    }

    /**
     * Delete user
     * 
     * @param id User ID
     * @return NotificationResponse
     */
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deleteUser(@PathVariable Long id) {
        NotificationResponse response = userService.deleteUser(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/admin/users/{id}/body-metrics
     * Lấy lịch sử body metrics của user (BodyMetricHistory)
     */
    @GetMapping("/{id}/body-metrics")
    public ResponseEntity<NotificationResponse> getUserBodyMetrics(
            @PathVariable Long id,
            @RequestParam(required = false) String from,
            @RequestParam(required = false) String to) {
        
        if (from != null && to != null) {
            java.time.ZonedDateTime fromDate = java.time.ZonedDateTime.parse(from);
            java.time.ZonedDateTime toDate = java.time.ZonedDateTime.parse(to);
            NotificationResponse response = bodyMetricHistoryService.getBodyMetricsByDateRange(id, fromDate, toDate);
            return ResponseEntity.ok(response);
        } else {
            NotificationResponse response = bodyMetricHistoryService.getAllBodyMetrics(id);
            return ResponseEntity.ok(response);
        }
    }

    /**
     * GET /api/admin/users/{id}/body-data
     * Lấy thông tin body hiện tại của user (InformationBodyUser)
     */
    @GetMapping("/{id}/body-data")
    public ResponseEntity<NotificationResponse> getUserBodyData(@PathVariable Long id) {
        NotificationResponse response = userInfoService.getUserInfo(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/admin/users/{id}/training-plans
     * Lấy danh sách training plans của user
     */
    @GetMapping("/{id}/training-plans")
    public ResponseEntity<NotificationResponse> getUserTrainingPlans(@PathVariable Long id) {
        NotificationResponse response = userTrainingService.getUserTrainingDetails(id);
        return ResponseEntity.ok(response);
    }

    /**
     * GET /api/admin/users/{userId}/training-plans/{utId}/personalized-details
     * Lấy tất cả PersonalizedPlanDetail của user cho một UserTraining cụ thể
     * Admin có thể xem và chỉnh sửa personalized plan của user
     */
    @GetMapping("/{userId}/training-plans/{utId}/personalized-details")
    public ResponseEntity<NotificationResponse> getUserPersonalizedPlanDetails(
            @PathVariable Long userId,
            @PathVariable Long utId) {
        try {
            NotificationResponse response = personalizationService.getAllPersonalizedPlanDetails(userId);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, 
                    "Error retrieving personalized plan details: " + e.getMessage()));
        }
    }

    /**
     * PUT /api/admin/users/{userId}/training-plans/{utId}/personalized-details/{ppdId}
     * Admin chỉnh sửa PersonalizedPlanDetail của user
     */
    @PutMapping("/{userId}/training-plans/{utId}/personalized-details/{ppdId}")
    public ResponseEntity<NotificationResponse> updateUserPersonalizedPlanDetail(
            @PathVariable Long userId,
            @PathVariable Long utId,
            @PathVariable Long ppdId,
            @RequestBody com.example.fitchallenge.DTO.PersonalizedPlanDetailDTO.PersonalizedPlanDetailResponse request) {
        try {
            NotificationResponse response = personalizationService.updatePersonalizedPlanDetail(ppdId, request);
            return ResponseEntity.ok(response);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.ok(new NotificationResponse(false, 
                    "Error updating personalized plan detail: " + e.getMessage()));
        }
    }
}



