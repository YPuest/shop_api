package com.example.shopapi.domain.service;

import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.repository.OrderItemRepository;
import org.springframework.stereotype.Component;

@Component
public class ProductDomainService {

    private final OrderItemRepository orderItemRepository;

    public ProductDomainService(OrderItemRepository orderItemRepository) {
        this.orderItemRepository = orderItemRepository;
    }

    public boolean isAvailable(Product product) {
        return product.isAvailable() && product.getStock() > 0;
    }

    public void reduceStock(Product product, int quantity) {
        if (product.getStock() < quantity) {
            throw new IllegalStateException("Not enough stock.");
        }
        product.setStock(product.getStock() - quantity);
    }

    public void ensureProductCanBeMarkedAsUnavailable(Long productId) {
        if (orderItemRepository.existsByProductId(productId)) {
            throw new IllegalStateException("Product is part of an active order and cannot be removed.");
        }
    }
}