package com.example.smartkrishi.models;

public class Reports {
    private int id;
    private String cropName;
    private String disease;
    private String recommendation;
    private int userId;
    private String createdAt;

    // Constructor without ID (for creating new reports before insert)
    public Reports(String cropName, String disease, String recommendation, int userId, String createdAt) {
        this.cropName = cropName;
        this.disease = disease;
        this.recommendation = recommendation;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Constructor with ID (for fetching from DB)
    public Reports(int id, String cropName, String disease, String recommendation, int userId, String createdAt) {
        this.id = id;
        this.cropName = cropName;
        this.disease = disease;
        this.recommendation = recommendation;
        this.userId = userId;
        this.createdAt = createdAt;
    }

    // Getters and Setters

    public int getId() {
        return id;
    }

    public String getCropName() {
        return cropName;
    }

    public void setCropName(String cropName) {
        this.cropName = cropName;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(String createdAt) {
        this.createdAt = createdAt;
    }
}
