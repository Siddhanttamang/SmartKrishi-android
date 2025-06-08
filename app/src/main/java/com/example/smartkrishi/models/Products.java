package com.example.smartkrishi.models;
public class Products {
    private String name;
    private String description;
    private String imageUrl;
    private int price;
    private String unit;
    private String category;
    private String seller;
    private String contact;
    private String location;

    public Products(String name, String description, String imageUrl, int price,
                   String unit, String category, String seller, String contact, String location) {
        this.name = name;
        this.description = description;
        this.imageUrl = imageUrl;
        this.price = price;
        this.unit = unit;
        this.category = category;
        this.seller = seller;
        this.contact = contact;
        this.location = location;
    }

    // Getters here (required for RecyclerView binding)
    public String getName() { return name; }
    public String getDescription() { return description; }
    public String getImageUrl() { return imageUrl; }
    public int getPrice() { return price; }
    public String getUnit() { return unit; }
    public String getCategory() { return category; }
    public String getSeller() { return seller; }
    public String getContact() { return contact; }
    public String getLocation() { return location; }
}
