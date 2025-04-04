package com.example.shopapi.web.dto;

import java.math.BigDecimal;

public class CartItemResponse {

    private Long productId;
    private String description;
    private BigDecimal price;
    private int quantity;

    public CartItemResponse(Long productId, String description, BigDecimal price, int quantity) {
        this.productId = productId;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public Long getProductId() {
        return productId;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getQuantity() {
        return quantity;
    }
}