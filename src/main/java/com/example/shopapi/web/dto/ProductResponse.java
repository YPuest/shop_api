package com.example.shopapi.web.dto;

import java.math.BigDecimal;

public class ProductResponse {

    private Long id;
    private String description;
    private BigDecimal price;
    private int stock;
    private boolean available;

    public ProductResponse(Long id, String description, BigDecimal price, int stock, boolean available) {
        this.id = id;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.available = available;
    }

    public Long getId() { return id; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
    public boolean isAvailable() { return available; }
}