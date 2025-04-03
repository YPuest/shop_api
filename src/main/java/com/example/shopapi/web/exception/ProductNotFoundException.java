package com.example.shopapi.web.exception;

public class ProductNotFoundException extends RuntimeException {
    public ProductNotFoundException(long id) {
        super("Product with ID " + id + " not found.");
    }
}