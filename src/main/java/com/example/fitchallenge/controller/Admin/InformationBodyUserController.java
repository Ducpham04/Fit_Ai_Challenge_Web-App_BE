package com.example.fitchallenge.controller.Admin;

import com.example.fitchallenge.DTO.InformationBodyDTO;
import com.example.fitchallenge.config.NotificationResponse;
import com.example.fitchallenge.service.InformationBodyUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/admin/information-body")
@RequiredArgsConstructor
public class InformationBodyUserController {

    private final InformationBodyUserService informationBodyUserService;

    // 🔹 [GET] Lấy tất cả bản ghi
    @GetMapping
    public ResponseEntity<NotificationResponse> getAllInformationBodyUsers() {
        NotificationResponse response = informationBodyUserService.getAllInformationBodyUsers();
        return ResponseEntity.ok(response);
    }

    // 🔹 [GET] Lấy danh sách theo userId
    @GetMapping("/{userId}")
    public ResponseEntity<NotificationResponse> getInformationByUserId(@PathVariable Long userId) {
        NotificationResponse response = informationBodyUserService.findByUserId(userId);
        return ResponseEntity.ok(response);
    }

    // 🔹 [POST] Tạo mới bản ghi
    @PostMapping
    public ResponseEntity<NotificationResponse> createInformationBodyUser(
            @RequestBody InformationBodyDTO dto) {
        NotificationResponse response = informationBodyUserService.createInformationBodyUser(dto);
        return ResponseEntity.ok(response);
    }

    // 🔹 [PUT] Cập nhật bản ghi
    @PutMapping("/{infoId}")
    public ResponseEntity<NotificationResponse> updateInformationBodyUser(
            @PathVariable Long infoId,
            @RequestBody InformationBodyDTO dto) {
        dto.setInfoId(infoId); // đảm bảo id được set
        NotificationResponse response = informationBodyUserService.updateInformationBodyUser(dto);
        return ResponseEntity.ok(response);
    }

    // 🔹 [DELETE] Xóa bản ghi theo id
    @DeleteMapping("/{id}")
    public ResponseEntity<NotificationResponse> deleteInformationBodyUser(@PathVariable Long id) {
        NotificationResponse response = informationBodyUserService.deleteInformationBodyUserById(id);
        return ResponseEntity.ok(response);
    }
}
