package com.example.shopapi.web.dto;

import com.example.shopapi.domain.model.valueobject.Price;

public class ProductUpdateRequest {
    private String description;
    private Price price;
    private int stock;

    public ProductUpdateRequest(String description, Price price, int stock) {
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public ProductUpdateRequest() {}

    public String getDescription() { return description; }
    public Price getPrice() { return price; }
    public int getStock() { return stock; }
}