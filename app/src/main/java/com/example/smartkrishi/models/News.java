package com.example.smartkrishi.models;

public class News {
    private String name;

    private String price;

    public News(String name, String price) {
        this.price = price;
        this.name = name;
    }

    public String getName() {
        return name;
    }
    public String getPrice() {
        return price;
    }
}
