package com.example.smartkrishi.models;

import java.util.List;

public class NewsResponse {
    private List<News> data;
    private String source;
    private String status;

    public List<News> getData() {
        return data;
    }

    public String getSource() {
        return source;
    }

    public String getStatus() {
        return status;
    }
}
