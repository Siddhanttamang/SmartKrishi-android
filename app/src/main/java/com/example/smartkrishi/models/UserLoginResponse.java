package com.example.smartkrishi.models;

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


        public UserData(int id, String email, String name) {
            this.id = id;
            this.email = email;
            this.name = name;
        }

        public int getId() {
            return id;
        }

        public void setId(int id) {
            this.id = id;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }
    }
}
