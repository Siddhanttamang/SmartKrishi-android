package com.example.smartkrishi.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import com.example.smartkrishi.R;

public class NewsFragments extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the updated news layout
        return inflater.inflate(R.layout.news, container, false);
    }
}