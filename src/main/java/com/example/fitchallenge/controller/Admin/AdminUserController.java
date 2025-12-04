package com.example.fitchallenge.controller.Admin;

import com.example.fitchallenge.DTO.user.RegisterRequestAdmin;
import com.example.fitchallenge.DTO.user.UserDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.UserService;
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
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int limit) {
        
        Pageable pageable = PageRequest.of(page, limit);
        Page<UserDTO> users = userService.getAllUsersPaginated(status, role, pageable);
        
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
}

