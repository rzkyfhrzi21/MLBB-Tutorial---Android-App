package com.example.mlbbtutorial.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.mlbbtutorial.R;

import java.util.List;

public class ItemCategoryAdapter extends RecyclerView.Adapter<ItemCategoryAdapter.VH> {

    public interface OnCategoryClick {
        void onClick(String category);
    }

    private final Context context;
    private final List<String> categories;
    private final OnCategoryClick listener;

    public ItemCategoryAdapter(Context context, List<String> categories, OnCategoryClick listener) {
        this.context = context;
        this.categories = categories;
        this.listener = listener;
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.row_item_category, parent, false);
        return new VH(v);
    }

    @Override
    public void onBindViewHolder(@NonNull VH h, int position) {
        String category = categories.get(position);

        h.txtCategory.setText(category);
        h.iconArrow.setImageResource(R.drawable.ic_arrow_right);

        h.itemView.setOnClickListener(v -> {
            if (listener != null) listener.onClick(category);
        });
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    static class VH extends RecyclerView.ViewHolder {
        TextView txtCategory;
        ImageView iconArrow;

        VH(@NonNull View itemView) {
            super(itemView);
            txtCategory = itemView.findViewById(R.id.txtCategory);
            iconArrow = itemView.findViewById(R.id.iconArrow);
        }
    }
}
