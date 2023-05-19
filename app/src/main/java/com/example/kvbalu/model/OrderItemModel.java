package com.example.kvbalu.model;

import java.io.Serializable;

public class OrderItemModel implements Serializable {
    private long id;

    private int quantity;

    private ProductModel product;

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public ProductModel getProduct() {
        return product;
    }

    public void setProduct(ProductModel product) {
        this.product = product;
    }
}
