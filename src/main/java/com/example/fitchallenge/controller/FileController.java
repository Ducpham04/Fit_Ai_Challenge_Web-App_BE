package com.example.fitchallenge.controller;

import com.example.fitchallenge.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/files")
public class FileController {

    @Autowired
    private FileStorageService fileStorageService;

    // Upload video
    @PostMapping("/upload-video")
    public ResponseEntity<String> uploadVideo(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.uploadFile(file);
        return ResponseEntity.ok(filePath);
    }

    // Upload hình ảnh (nếu cần riêng)
    @PostMapping("/upload-image")
    public ResponseEntity<String> uploadImage(@RequestParam("file") MultipartFile file) {
        String filePath = fileStorageService.uploadFile(file);
        return ResponseEntity.ok(filePath);
    }
}
