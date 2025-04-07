package com.example.shopapi.web.dto;

public class CheckoutResponse {

    private final Long orderId;
    private final String message;

    public CheckoutResponse(Long orderId, String message) {
        this.orderId = orderId;
        this.message = message;
    }

    public Long getOrderId() {
        return orderId;
    }

    public String getMessage() {
        return message;
    }
}