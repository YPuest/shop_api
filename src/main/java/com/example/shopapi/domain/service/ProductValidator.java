package com.example.shopapi.domain.service;

import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator {

    public void validate(String description, int stock) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be empty.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
    }
}