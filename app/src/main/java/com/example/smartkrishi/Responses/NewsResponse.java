package com.example.smartkrishi.Responses;

import com.example.smartkrishi.models.News;

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
