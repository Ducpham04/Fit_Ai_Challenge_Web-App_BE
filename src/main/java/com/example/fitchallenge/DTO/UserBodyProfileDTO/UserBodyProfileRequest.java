package com.example.fitchallenge.DTO.UserBodyProfileDTO;

import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class UserBodyProfileRequest {
    // height: cm, thường 140–220 cm cho người lớn (cho phép trẻ em/vận động viên)
    @Min(value = 100, message = "Height must be at least 100 cm")
    @Max(value = 250, message = "Height cannot exceed 250 cm")
    private Double height;

    // weight: kg, 30–200 kg phổ biến, cho phép người rất nhẹ/nặng
    @Min(value = 20, message = "Weight must be at least 20 kg")
    @Max(value = 300, message = "Weight cannot exceed 300 kg")
    private Double weight;

    // body fat %: 3–60% là phạm vi thực tế (dưới 3% rất hiếm và nguy hiểm)
    @Min(value = 3, message = "Body fat must be at least 3%")
    @Max(value = 60, message = "Body fat cannot exceed 60%")
    private Double bodyFat;

    // muscle mass (skeletal muscle): thường 15–100 kg tùy người
    @Min(value = 5, message = "Muscle mass must be at least 5 kg")
    @Max(value = 150, message = "Muscle mass cannot exceed 150 kg")
    private Double muscleMass;

    // age: 10–120 (cho phép trẻ vị thành niên + người cao tuổi)
    @Min(value = 10, message = "Age must be at least 10")
    @Max(value = 120, message = "Age cannot exceed 120")
    private Integer age;

    // gender: chỉ cho phép vài giá trị hợp lý
    @Pattern(regexp = "^(male|female|other|prefer_not_to_say)$",
            message = "Gender must be 'male', 'female', 'other' or 'prefer_not_to_say'")
    private String gender;

    // experienceLevel & goal: giữ nguyên như cũ
    @Pattern(regexp = "^(beginner|intermediate|advanced)$",
            message = "Experience level must be beginner, intermediate or advanced")
    private String experienceLevel;

    @Pattern(regexp = "^(lose_weight|build_muscle|maintain_fitness|improve_endurance)$",
            message = "Goal must be one of: lose_weight, build_muscle, maintain_fitness, improve_endurance")
    private String goal;

    // injuryNotes: chỉ giới hạn độ dài
    @Size(max = 500, message = "Injury notes cannot exceed 500 characters")
    private String injuryNotes;
}




