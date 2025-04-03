package com.example.shopapi.domain.service;

import com.example.shopapi.domain.model.valueobject.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator {

    public void validate(String description, Stock stock) {
        if (description == null || description.isBlank()) {
            throw new IllegalArgumentException("Description must not be empty.");
        }
        if (stock.getQuantity() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
    }
}