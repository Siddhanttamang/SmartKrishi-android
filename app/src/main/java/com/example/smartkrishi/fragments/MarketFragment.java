package com.example.smartkrishi.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.smartkrishi.R;
import com.example.smartkrishi.adapters.ProductAdapter;
import com.example.smartkrishi.models.Products;

import java.util.ArrayList;
import java.util.List;

public class MarketFragment extends Fragment {

    private List<Products> productList;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater,
                             @Nullable ViewGroup container,
                             @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_market, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view,
                              @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        RecyclerView recyclerView = view.findViewById(R.id.marketRecyclerView);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        productList = new ArrayList<>();

        productList.add(new Products(
                "Organic Tomato",
                "Fresh organic tomatoes from local farm.",
                "https://example.com/tomato.jpg",
                80,
                "kg",
                "Vegetables",
                "Farmer Ganesh",
                "9801234567",
                "Chitwan"
        ));

        productList.add(new Products(
                "Basmati Rice",
                "Premium quality basmati rice.",
                "https://example.com/rice.jpg",
                120,
                "kg",
                "Grains",
                "Farmer Ramesh",
                "9812345678",
                "Jhapa"
        ));

        productList.add(new Products(
                "Organic Basil Seeds",
                "High-quality organic basil seeds for your kitchen garden.",
                "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=500&q=80",
                50,
                "pack",
                "Seeds",
                "GreenFarm Co.",
                "9801112233",
                "Kathmandu"
        ));

        productList.add(new Products(
                "Aloe Vera Plant",
                "Fresh aloe vera plant, perfect for skincare and home decoration.",
                "https://images.unsplash.com/photo-1506744038136-46273834b3fb?auto=format&fit=crop&w=500&q=80",
                150,
                "piece",
                "Plants",
                "Nature’s Gift",
                "9802223344",
                "Lalitpur"
        ));

        productList.add(new Products(
                "Gardening Shovel",
                "Durable gardening shovel for all your planting needs.",
                "https://images.unsplash.com/photo-1469474968028-56623f02e42e?auto=format&fit=crop&w=500&q=80",
                800,
                "piece",
                "Tools",
                "ToolMasters",
                "9803334455",
                "Bhaktapur"
        ));

        productList.add(new Products(
                "Indoor Succulent Set",
                "Set of 3 indoor succulents with decorative pots.",
                "https://images.unsplash.com/photo-1528825871115-3581a5387919?auto=format&fit=crop&w=500&q=80",
                1200,
                "set",
                "Plants",
                "Succulent World",
                "9804445566",
                "Pokhara"
        ));

        productList.add(new Products(
                "Organic Tomato Seeds",
                "Certified organic tomato seeds with high yield potential.",
                "https://images.unsplash.com/photo-1578985545062-69928b1d9587?auto=format&fit=crop&w=500&q=80",
                70,
                "pack",
                "Seeds",
                "FreshRoots",
                "9805556677",
                "Chitwan"
        ));

        productList.add(new Products(
                "Eco-friendly Watering Can",
                "Lightweight and durable watering can made from recycled materials.",
                "https://images.unsplash.com/photo-1516715094485-93b5b0a3ed82?auto=format&fit=crop&w=500&q=80",
                950,
                "piece",
                "Tools",
                "GreenGrow Supplies",
                "9806667788",
                "Biratnagar"
        ));

        productList.add(new Products(
                "Lavender Plant",
                "Fragrant lavender plant for garden and aromatherapy.",
                "https://images.unsplash.com/photo-1517694712202-14dd9538aa97?auto=format&fit=crop&w=500&q=80",
                300,
                "piece",
                "Plants",
                "Bloom & Grow",
                "9807778899",
                "Dharan"
        ));

        productList.add(new Products(
                "Garden Fertilizer Bag",
                "Organic garden fertilizer to boost plant growth.",
                "https://images.unsplash.com/photo-1501004318641-b39e6451bec6?auto=format&fit=crop&w=500&q=80",
                900,
                "kg",
                "Fertilizers",
                "EarthCare",
                "9808889900",
                "Kathmandu"
        ));

        productList.add(new Products(
                "Herb Garden Kit",
                "Complete herb garden starter kit with seeds and soil.",
                "https://images.unsplash.com/photo-1486312338219-ce68d2c6f44d?auto=format&fit=crop&w=500&q=80",
                1500,
                "kit",
                "Gardening",
                "Urban Planters",
                "9809990011",
                "Lalitpur"
        ));

        productList.add(new Products(
                "Raised Garden Bed Kit",
                "Easy to assemble raised garden bed kit for backyard gardening.",
                "https://images.unsplash.com/photo-1528825871115-3581a5387919?auto=format&fit=crop&w=500&q=80",
                3500,
                "kit",
                "Gardening",
                "GreenThumb",
                "9810001122",
                "Bhaktapur"
        ));


        ProductAdapter adapter = new ProductAdapter(productList);
        recyclerView.setAdapter(adapter);
    }
}
