package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.bumptech.glide.Glide;
import com.example.smartkrishi.R;
import com.example.smartkrishi.models.Reports;

public class ReportDetailsDialogFragment extends DialogFragment {

    private static final String ARG_REPORT = "report";
    private Reports report;

    public static ReportDetailsDialogFragment newInstance(Reports report) {
        ReportDetailsDialogFragment fragment = new ReportDetailsDialogFragment();
        Bundle args = new Bundle();
        args.putSerializable(ARG_REPORT, report);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setStyle(DialogFragment.STYLE_NORMAL, android.R.style.Theme_Material_Light_Dialog_Alert);
        if (getArguments() != null) {
            report = (Reports) getArguments().getSerializable(ARG_REPORT);
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_report_details, container, false);

        TextView tvCropName = view.findViewById(R.id.tvCropName);
        TextView tvDisease = view.findViewById(R.id.tvDisease);
        TextView tvRecommendation = view.findViewById(R.id.tvRecommendation);
        ImageView ivProductImage = view.findViewById(R.id.ivProductImage);

        if (report != null ) {
            tvCropName.setText(report.getCrop_name());
            tvDisease.setText(report.getDisease());
            tvRecommendation.setText(report.getRecommendation());

            Glide.with(requireContext())
                    .load(report.getImage_url())
                    .placeholder(R.drawable.ic_launcher_foreground)
                    .into(ivProductImage);
        }

        return view;
    }
}
