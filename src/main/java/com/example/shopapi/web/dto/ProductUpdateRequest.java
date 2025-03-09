package com.example.shopapi.web.dto;

import java.math.BigDecimal;

public class ProductUpdateRequest {
    private String description;
    private BigDecimal price;
    private int stock;

    public ProductUpdateRequest(String description, BigDecimal price, int stock) {
        this.description = description;
        this.price = price;
        this.stock = stock;
    }

    public ProductUpdateRequest() {}

    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}