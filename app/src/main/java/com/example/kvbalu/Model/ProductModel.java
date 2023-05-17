package com.example.kvbalu.Model;


import java.util.List;

public class ProductModel {
    private long id;
    private String productName;
    private int quantity;
    private int sold;
    private String description;
    private int price;
    private Boolean isActive = true;
    private CategoryModel category;
    private List<ProductImageModel> productImages;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public int getSold() {
        return sold;
    }

    public String getDescription() {
        return description;
    }

    public int getPrice() {
        return price;
    }

    public Boolean getActive() {
        return isActive;
    }

    public CategoryModel getCategory() {
        return category;
    }

    public List<ProductImageModel> getProductImages() {
        return productImages;
    }

}
