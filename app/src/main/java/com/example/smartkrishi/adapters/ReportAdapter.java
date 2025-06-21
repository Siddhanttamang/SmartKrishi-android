package com.example.smartkrishi.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.smartkrishi.R;
import com.example.smartkrishi.models.Reports;

import java.util.List;

public class ReportAdapter extends RecyclerView.Adapter<ReportAdapter.ReportViewHolder> {

    private final List<Reports> reportsList;
    private OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(Reports report);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    public ReportAdapter(List<Reports> reportsList) {
        this.reportsList = reportsList;
    }

    public class ReportViewHolder extends RecyclerView.ViewHolder {
        TextView cropName, disease, recommendation, createdAt;
        ImageView reportImage;

        public ReportViewHolder(@NonNull View itemView) {
            super(itemView);
            cropName = itemView.findViewById(R.id.cropName);
            disease = itemView.findViewById(R.id.disease);
            recommendation = itemView.findViewById(R.id.recommendation);
            createdAt = itemView.findViewById(R.id.createdAt);
            reportImage = itemView.findViewById(R.id.reportImage);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (listener != null && position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(reportsList.get(position));
                }
            });
        }

        // Optional: clean way to bind views
        public void bind(Reports report) {
            cropName.setText(report.getCrop_name());
            disease.setText(report.getDisease());
            recommendation.setText(report.getRecommendation());
            createdAt.setText(report.getCreated_at());

            Glide.with(itemView.getContext())
                    .load(report.getImage_url())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(reportImage);
        }
    }

    @NonNull
    @Override
    public ReportViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_report, parent, false);
        return new ReportViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReportViewHolder holder, int position) {
        Reports report = reportsList.get(position);
        holder.bind(report); // Use bind method
    }

    @Override
    public int getItemCount() {
        return reportsList.size();
    }
}
