package com.example.smartkrishi.Responses;

public class UserLoginResponse {
    private String access_token;
    private UserData user;

    public String getAccessToken() {
        return access_token;
    }

    public UserData getUser() {
        return user;
    }

    public static class UserData {
        private int id;
        private String name;
        private String email;
        private String address;
        private String contact;

        public UserData(int id, String contact, String address, String email, String name) {
            this.id = id;
            this.contact = contact;
            this.address = address;
            this.email = email;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
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

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }
    }
}
