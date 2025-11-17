package com.example.FIT_Challenge.service;

import org.springframework.web.multipart.MultipartFile;
// Lưu trữ File hình ảnh lên server hoặc cloud
public interface FileStorageService {
    String uploadFile(MultipartFile file) ;
}
