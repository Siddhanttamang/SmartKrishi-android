package com.example.smartkrishi.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartkrishi.R;
import com.example.smartkrishi.models.News;

import java.util.ArrayList;
import java.util.List;

public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.NewsViewHolder> {

    private final List<News> originalList;
    private List<News> filteredList;

    public NewsAdapter(List<News> newsList) {
        this.originalList = newsList;
        this.filteredList = new ArrayList<>(newsList);
    }

    public static class NewsViewHolder extends RecyclerView.ViewHolder {
        TextView newsTitle, newsPrice;

        public NewsViewHolder(@NonNull View itemView) {
            super(itemView);
            newsTitle = itemView.findViewById(R.id.newsTitle);
            newsPrice = itemView.findViewById(R.id.newsPrice);
        }
    }

    @NonNull
    @Override
    public NewsViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_news, parent, false);
        return new NewsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull NewsViewHolder holder, int position) {
        News news = filteredList.get(position);
        holder.newsTitle.setText(news.getName());
        holder.newsPrice.setText(news.getPrice());
    }

    @Override
    public int getItemCount() {
        return filteredList.size();
    }

    // Add this method for filtering
    public void filter(String query) {
        filteredList = new ArrayList<>();
        if (query == null || query.trim().isEmpty()) {
            filteredList.addAll(originalList);
        } else {
            for (News news : originalList) {
                if (news.getName().toLowerCase().contains(query.toLowerCase())) {
                    filteredList.add(news);
                }
            }
        }
        notifyDataSetChanged();
    }
}
