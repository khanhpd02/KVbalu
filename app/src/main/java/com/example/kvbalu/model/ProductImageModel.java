package com.example.kvbalu.model;

import java.io.Serializable;

public class ProductImageModel implements Serializable {

    private long id;

    private String image;


    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getImage() {
        return image;
    }


}
