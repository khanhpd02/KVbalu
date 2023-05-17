package com.example.kvbalu.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.viewpager.widget.PagerAdapter;

import com.bumptech.glide.Glide;
import com.example.kvbalu.Model.ProductImageModel;
import com.example.kvbalu.R;

import java.util.List;

public class ProductImageAdapter extends PagerAdapter {
    private final Context mContext;
    private final List<ProductImageModel> productImages;

    public ProductImageAdapter(Context mContext, List<ProductImageModel> productImages) {
        this.mContext = mContext;
        this.productImages = productImages;
    }

    @Override
    public int getCount() {
        if(productImages != null){
            return productImages.size();
        }
        return 0;
    }

    @Override
    public boolean isViewFromObject(@NonNull View view, @NonNull Object object) {
        return view == object;
    }

    @NonNull
    @Override
    public Object instantiateItem(@NonNull ViewGroup container, int position) {
        View view = LayoutInflater.from(container.getContext()).inflate(R.layout.product_image_slider, container, false);

        ImageView img = view.findViewById(R.id.ivProductImage);
        ProductImageModel productImage = productImages.get(position);
        if(productImage != null){
            Glide.with(mContext).load(productImage.getImage()).into(img);
        }
        container.addView(view);
        return view;
    }

    @Override
    public void destroyItem(@NonNull ViewGroup container, int position, @NonNull Object object) {
        container.removeView((View) object);
    }
}
