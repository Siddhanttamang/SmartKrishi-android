package com.example.smartkrishi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.smartkrishi.R;

import com.example.smartkrishi.models.Reports;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<Reports> reportsList;

    public ReportAdapter(List<Reports> reportsList) {
        this.reportsList = reportsList;
    }

    public static class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView cropName, disease,recommendation,createdAt;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropName);
            disease = itemView.findViewById(R.id.disease);
            recommendation= itemView.findViewById(R.id.recommendation);
            createdAt=itemView.findViewById(R.id.createdAt);
        }
    }

    @NonNull
    @Override
    public ReportAdapter.ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_report, parent, false);
        return new ReportAdapter.ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportAdapter.ReportViewHolder holder, int position) {
       Reports reports = reportsList.get(position);
       holder.cropName.setText(reports.getCropName());
       holder.disease.setText(reports.getDisease());
       holder.recommendation.setText(reports.getRecommendation());
       holder.createdAt.setText((reports.getCreatedAt()));
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}


