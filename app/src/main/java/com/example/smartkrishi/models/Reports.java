package com.example.smartkrishi.models;
import java.io.Serializable;

public class Reports implements Serializable{

    private String crop_name;
    private String disease;
    private String recommendation;
    private int user_id;
    private String image_url;
    private String created_at;

    public Reports(String crop_name, String created_at, String image_url, int user_id, String recommendation, String disease) {
        this.crop_name = crop_name;
        this.created_at = created_at;
        this.image_url = image_url;
        this.user_id = user_id;
        this.recommendation = recommendation;
        this.disease = disease;
    }

    public String getCrop_name() {
        return crop_name;
    }

    public void setCrop_name(String crop_name) {
        this.crop_name = crop_name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public String getCreated_at() {
        return created_at;
    }

    public void setCreated_at(String created_at) {
        this.created_at = created_at;
    }

    public int getUser_id() {
        return user_id;
    }

    public void setUser_id(int user_id) {
        this.user_id = user_id;
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