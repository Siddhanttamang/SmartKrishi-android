package com.example.smartkrishi.models;

public class Recommendation {
    public String crop;
    public String disease;
    public String recommendation;

    public Recommendation() {
    }

    public Recommendation(String recommendation, String disease, String crop) {
        this.recommendation = recommendation;
        this.disease = disease;
        this.crop = crop;
    }

    public String getCrop() {
        return crop;
    }

    public void setCrop(String crop) {
        this.crop = crop;
    }

    public String getRecommendation() {
        return recommendation;
    }

    public void setRecommendation(String recommendation) {
        this.recommendation = recommendation;
    }

    public String getDisease() {
        return disease;
    }

    public void setDisease(String disease) {
        this.disease = disease;
    }
}