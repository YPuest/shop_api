package com.example.shopapi.web.dto;

import java.math.BigDecimal;

public class ProductRequest {
    private String name;
    private String description;
    private BigDecimal price;
    private int stock;
    private Long categoryId;

    // Getters
    public String getName() { return name; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public Long getCategoryId() { return categoryId; }
}