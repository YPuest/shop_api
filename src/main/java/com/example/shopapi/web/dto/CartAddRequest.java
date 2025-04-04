package com.example.shopapi.web.dto;

public class CartAddRequest {

    private Long customerId;
    private Long productId;
    private int quantity;

    public CartAddRequest() {}

    public CartAddRequest(Long customerId, Long productId, int quantity) {
        this.customerId = customerId;
        this.productId = productId;
        this.quantity = quantity;
    }

    public Long getCustomerId() {
        return customerId;
    }

    public Long getProductId() {
        return productId;
    }

    public int getQuantity() {
        return quantity;
    }
}