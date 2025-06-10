package com.example.smartkrishi.models;
public class Products {
    private String name;
    private int quantity;
    private int price;
    private String imageUrl;
    private String seller;
    private String address;
    private String contact;

    public Products(String address, String contact, String seller, String imageUrl, int price, int quantity, String name) {
        this.address = address;
        this.contact = contact;
        this.seller = seller;
        this.imageUrl = imageUrl;
        this.price = price;
        this.quantity = quantity;
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getContact() {
        return contact;
    }

    public void setContact(String contact) {
        this.contact = contact;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getSeller() {
        return seller;
    }

    public void setSeller(String seller) {
        this.seller = seller;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }
}