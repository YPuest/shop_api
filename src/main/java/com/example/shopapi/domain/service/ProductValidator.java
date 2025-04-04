package com.example.shopapi.domain.service;

import com.example.shopapi.domain.model.valueobject.ProductDescription;
import com.example.shopapi.domain.model.valueobject.Stock;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

@Component
public class ProductValidator {

    public void validate(ProductDescription description, Stock stock) {
        if (description == null) {
            throw new IllegalArgumentException("Description must not be null.");
        }
        if (stock.getQuantity() < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }
    }
}