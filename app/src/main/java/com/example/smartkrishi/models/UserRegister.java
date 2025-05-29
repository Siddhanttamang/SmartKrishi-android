package com.example.smartkrishi.models;

public class UserRegister {
    private String name;
    private String email;
    private String address;
    private String password;
    private String phone;

    public UserRegister(String name, String phone, String password, String address, String email) {
        this.name = name;
        this.phone = phone;
        this.password = password;
        this.address = address;
        this.email = email;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
