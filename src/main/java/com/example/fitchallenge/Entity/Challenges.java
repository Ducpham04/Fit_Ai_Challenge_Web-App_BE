package com.example.fitchallenge.Entity;


import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "Challenges")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Challenges {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name="challenge_id")
    private Long id;

    // Đây là quan hệ nhiều thử thách thuộc 1 mục tiêu
    @ManyToOne
    @JoinColumn(name = "goal_id") // ✅ sửa ở đây
    private Goals goal;

    @Column(name = "Name_Challenge")
    private String title;

    @Column(name = "Description")
    private String description;

    @Enumerated(EnumType.STRING)
    @Column(name = "DifficultLevel")
    private DifficultLevel difficult;

    public enum DifficultLevel {
        EASY, MEDIUM, HARD
    }

    @Column(name = "Link_Videos")
    private String linkVideos;
    
    @Column(name = "Exercise_Type")
    private String exerciseType; // AI model/exercise type: push-up, squat, pull-up, sit-up, plank
    
    @Column(name = "Reward")
    private String reward; // Reward description for completing challenge

    @Enumerated(EnumType.STRING)
    @Column(name="Status")
    private Status status;

    public enum  Status {
        ACTIVE, INACTIVE, DRAFT, COMPLETED
    }
    
    // Helper method to convert linkVideos string to array
    public java.util.List<String> getVideoArray() {
        if (linkVideos == null || linkVideos.isEmpty()) {
            return new java.util.ArrayList<>();
        }
        return java.util.Arrays.asList(linkVideos.split(","));
    }
}
