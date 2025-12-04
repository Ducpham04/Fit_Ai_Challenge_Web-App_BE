package com.example.fitchallenge.config;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;


public class CloudinaryConfig {

    public Cloudinary cloudinary() {
        return new Cloudinary(ObjectUtils.asMap(
                "cloud_name", "your_cloud_name",
                "api_key", "your_api_key",
                "api_secret", "your_api_secret"));
    }
}
