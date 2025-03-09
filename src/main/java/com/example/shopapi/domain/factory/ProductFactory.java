package com.example.shopapi.domain.factory;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import java.math.BigDecimal;

public class ProductFactory {

    public static Product createProduct(String description, BigDecimal price, int stock, Category category) {
        return new Product(description, price, stock, category);
    }

    public static Product updateProduct(Product existingProduct, String description, BigDecimal price, int stock) {
        return new Product(description, price, stock, existingProduct.getCategory());
    }

    public static Product markAsUnavailable(Product product) {
        product.markAsUnavailable();

        return product;
    }
}