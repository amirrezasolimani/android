package com.example.p2;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import java.util.List;

public class VerticalAdapter extends RecyclerView.Adapter<VerticalAdapter.VHolder> {

    private List<CategoryItem> categories;
    private OnUserClickListener listener;

    public VerticalAdapter(List<CategoryItem> categories, OnUserClickListener listener) {
        this.categories = categories;
        this.listener = listener;
    }

    @Override
    public VHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_vertical, parent, false);
        return new VHolder(view);
    }

    @Override
    public void onBindViewHolder(VHolder holder, int position) {
        holder.bind(categories.get(position));
    }

    @Override
    public int getItemCount() {
        return categories.size();
    }

    class VHolder extends RecyclerView.ViewHolder {
        TextView tvCategory;
        RecyclerView rvHorizontal;

        VHolder(View itemView) {
            super(itemView);
            tvCategory = itemView.findViewById(R.id.tvCategory);
            rvHorizontal = itemView.findViewById(R.id.rvHorizontal);
        }

        void bind(CategoryItem categoryItem) {
            tvCategory.setText(categoryItem.getCategoryName());

            rvHorizontal.setLayoutManager(
                    new LinearLayoutManager(itemView.getContext(), LinearLayoutManager.HORIZONTAL, false)
            );

            HorizontalUserAdapter adapter = new HorizontalUserAdapter(categoryItem.getUsers(), listener);
            rvHorizontal.setAdapter(adapter);
        }
    }
}}
