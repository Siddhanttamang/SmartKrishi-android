package com.example.smartkrishi.models;



public class UserUpdateRequest {
    private String name;
    private String email;
    private String address;
    private String contact;
    private String password;

    public UserUpdateRequest(String name, String email, String address, String contact, String password) {
        this.name = name;
        this.email = email;
        this.address = address;
        this.contact = contact;
        this.password = password;
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

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
