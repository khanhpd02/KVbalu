package com.example.kvbalu.Adapter;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.kvbalu.Interface.CategoryRecycleInterface;
import com.example.kvbalu.Model.CategoryModel;
import com.example.kvbalu.R;

import java.util.List;

public class CategoryAdapter extends RecyclerView.Adapter<CategoryAdapter.ViewHolder> {
    private final CategoryRecycleInterface categoryRecycleInterface;
    List<CategoryModel> categoryModels;
    Context context;

    public CategoryAdapter(List<CategoryModel> categories, Context context, CategoryRecycleInterface categoryRecycleInterface) {
        this.categoryModels = categories;
        this.context = context;
        this.categoryRecycleInterface = categoryRecycleInterface;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        TextView categoryId;
//        TextView tvUserAction;

        public ViewHolder(@NonNull View itemView, CategoryRecycleInterface categoryRecycleInterface) {
            super(itemView);
            imageView = itemView.findViewById(R.id.ivCategory);
            textView = itemView.findViewById(R.id.tvCategory);
//            layout = itemView.findViewById(R.id.clCategory);
            categoryId = itemView.findViewById(R.id.tvCategoryId);
//            tvUserAction = itemView.findViewById(R.id.tvUserAction);

            itemView.setOnClickListener(v -> {
                if(categoryRecycleInterface != null){
                    int position = getAdapterPosition();
                    if(position != RecyclerView.NO_POSITION){
                        categoryRecycleInterface.onCategoryItemClick(position);
                    }
                }
            });
        }
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.viewholder_recycle_category_no_img, parent, false);
        return new ViewHolder(view, categoryRecycleInterface);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.textView.setText(categoryModels.get(position).getName());
        holder.categoryId.setText(String.valueOf(categoryModels.get(position).getId()));
    }

    @Override
    public int getItemCount() {
        return categoryModels.size();
    }


}
