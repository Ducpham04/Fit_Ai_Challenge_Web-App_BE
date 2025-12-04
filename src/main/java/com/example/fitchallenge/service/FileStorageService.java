package com.example.fitchallenge.service;

import org.springframework.web.multipart.MultipartFile;
// Lưu trữ File hình ảnh lên server hoặc cloud
public interface FileStorageService {
    String uploadFile(MultipartFile file) ;
}
