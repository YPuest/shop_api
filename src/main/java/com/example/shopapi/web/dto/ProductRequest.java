package com.example.shopapi.web.dto;

import com.example.shopapi.domain.model.valueobject.Price;

public class ProductRequest {
    private String name;
    private String description;
    private Price price;
    private int stock;
    private Long categoryId;

    public String getName() { return name; }
    public String getDescription() { return description; }
    public Price getPrice() { return price; }
    public int getStock() { return stock; }
    public Long getCategoryId() { return categoryId; }
}