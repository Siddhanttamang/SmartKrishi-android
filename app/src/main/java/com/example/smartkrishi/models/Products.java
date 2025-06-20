package com.example.smartkrishi.models;
public class Products {
    private String name;
    private int quantity;
    private int price;
    private String image_url;
    private String user_name;
    private String user_address;
    private String user_contact;


    public Products(String name, String user_contact, String user_address, String user_name, int price, String image_url, int quantity) {
        this.name = name;
        this.user_contact = user_contact;
        this.user_address = user_address;
        this.user_name = user_name;
        this.price = price;
        this.image_url = image_url;
        this.quantity = quantity;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getImage_url() {
        return image_url;
    }

    public void setImage_url(String image_url) {
        this.image_url = image_url;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public String getUser_address() {
        return user_address;
    }

    public void setUser_address(String user_address) {
        this.user_address = user_address;
    }

    public String getUser_contact() {
        return user_contact;
    }

    public void setUser_contact(String user_contact) {
        this.user_contact = user_contact;
    }
}