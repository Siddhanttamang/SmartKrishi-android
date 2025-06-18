package com.example.smartkrishi.models;

public class Reports {

    private String crop_name;
    private String disease;
    private String recommendation;
    private int user_id;
    private String created_at;

    public Reports(String crop_name, int user_id, String created_at, String recommendation, String disease) {
        this.crop_name = crop_name;
        this.user_id = user_id;
        this.created_at = created_at;
        this.recommendation = recommendation;
        this.disease = disease;
    }

    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
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