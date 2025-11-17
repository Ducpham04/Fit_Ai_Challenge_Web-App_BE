package com.example.FIT_Challenge.Entity;

import jakarta.persistence.*;

@Entity
@Table(name = "training_plan_details")
public class TrainingPlanDetail {
    // Mô tả bảng lưu trữ kế hoạch từng ngày
    // 🆔 Mã chi tiết kế hoạch (primary key, tự động tăng)
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "tpd_id")
    private Long tpdId;

    // 🔗 Kế hoạch gốc (một kế hoạch có nhiều chi tiết)
    @ManyToOne
    @JoinColumn(name = "tp_id")
    private TrainingPlan trainingPlan;

    // 📅 Ngày trong kế hoạch (bắt buộc nhập)
    @Column(name = "day_number", nullable = false)
    private Integer dayNumber;

    // 🏋️‍♂️ Thử thách gắn với ngày tập (một thử thách có thể nằm trong nhiều kế hoạch)
    @ManyToOne
    @JoinColumn(name = "challenge_id")
    private Challenges challenge;

    // 🔁 Số hiệp (sets), mặc định = 1
    @Column(name = "sets", columnDefinition = "INTEGER DEFAULT 1")
    private Integer sets = 1;

    // 🔂 Số lần lặp trong mỗi hiệp (có thể null nếu không áp dụng)
    @Column(name = "reps")
    private Integer reps;

    // 🧱 Constructors
    public TrainingPlanDetail() {}

    public TrainingPlanDetail(TrainingPlan trainingPlan, Integer dayNumber, Challenges challenge, Integer sets, Integer reps) {
        this.trainingPlan = trainingPlan;
        this.dayNumber = dayNumber;
        this.challenge = challenge;
        this.sets = sets;
        this.reps = reps;
    }

    // 🧩 Getters và Setters
    public Long getTpdId() {
        return tpdId;
    }

    public void setTpdId(Long tpdId) {
        this.tpdId = tpdId;
    }

    public TrainingPlan getTrainingPlan() {
        return trainingPlan;
    }

    public void setTrainingPlan(TrainingPlan trainingPlan) {
        this.trainingPlan = trainingPlan;
    }

    public Integer getDayNumber() {
        return dayNumber;
    }

    public void setDayNumber(Integer dayNumber) {
        this.dayNumber = dayNumber;
    }

    public Challenges getChallenge() {
        return challenge;
    }

    public void setChallenge(Challenges challenge) {
        this.challenge = challenge;
    }

    public Integer getSets() {
        return sets;
    }

    public void setSets(Integer sets) {
        this.sets = sets;
    }

    public Integer getReps() {
        return reps;
    }

    public void setReps(Integer reps) {
        this.reps = reps;
    }
}
