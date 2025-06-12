package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartkrishi.Database.NewsDAO;
import com.example.smartkrishi.R;
import com.example.smartkrishi.adapters.NewsAdapter;
import com.example.smartkrishi.models.News;
import com.example.smartkrishi.Services.NewsService;

import java.util.Collections;
import java.util.List;

public class NewsFragments extends Fragment {

    private RecyclerView newsRecyclerView;
    private NewsAdapter newsAdapter;
    private View newsLoading; // Can be ProgressBar or LottieAnimationView


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.news, container, false);

        newsRecyclerView = view.findViewById(R.id.newsRecyclerView);
        newsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        newsLoading = view.findViewById(R.id.newsLoading); // ProgressBar or Lottie

        NewsDAO newsDAO = new NewsDAO(requireContext());

        // Load cached news first
        List<News> cachedNews = newsDAO.getAllNews();
        Collections.reverse(cachedNews);
        if (!cachedNews.isEmpty()) {

            newsAdapter = new NewsAdapter(cachedNews);
            newsRecyclerView.setAdapter(newsAdapter);
            newsRecyclerView.setVisibility(View.VISIBLE);
            newsLoading.setVisibility(View.GONE);
        } else {
            // If no cached data, show loading
            newsRecyclerView.setVisibility(View.GONE);
            newsLoading.setVisibility(View.VISIBLE);
        }

        // Always try to fetch latest news from API
        NewsService newsService = new NewsService();
        newsService.fetchNews(new NewsService.NewsCallback() {
            @Override
            public void onSuccess(List<News> newsList) {

                newsAdapter = new NewsAdapter(newsList);
                newsRecyclerView.setAdapter(newsAdapter);

                // Save to SQLite
                for (News news : newsList) {
                    newsDAO.insertNews(news);
                }

                newsLoading.setVisibility(View.GONE);
                newsRecyclerView.setVisibility(View.VISIBLE);
            }

            @Override
            public void onFailure(String errorMessage) {
                Toast.makeText(getContext(), "Failed to fetch news: " + errorMessage, Toast.LENGTH_SHORT).show();
                newsLoading.setVisibility(View.GONE);
                // Don't hide recycler if cache was shown
                if (cachedNews.isEmpty()) {
                    newsRecyclerView.setVisibility(View.GONE);
                }
            }
        });

        return view;
    }

}
