package com.example.shopapi.domain.factory;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.model.valueobject.Stock;

public class ProductFactory {

    public static Product createProduct(String description, Price price, Stock stock, Category category) {
        return new Product(description, price, stock, category);
    }

    public static Product updateProduct(Product existingProduct, String description, Price price, Stock stock) {
        return new Product(description, price, stock, existingProduct.getCategory());
    }

    public static Product markAsUnavailable(Product product) {
        product.markAsUnavailable();

        return product;
    }
}