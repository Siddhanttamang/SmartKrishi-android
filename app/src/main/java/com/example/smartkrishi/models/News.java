package com.example.smartkrishi.models;

public class News {
    private String name;

    private String price;
    private String updated_at;

    public News(String name, String price,String updated_at) {
        this.price = price;
        this.updated_at=updated_at;
        this.name = name;
    }
    public String getUpdated_at() {

        return updated_at;
    }

    public void setUpdated_on(String updated_at) {

        this.updated_at = updated_at;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
}
