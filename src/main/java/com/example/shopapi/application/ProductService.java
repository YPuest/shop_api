package com.example.shopapi.application;

import com.example.shopapi.domain.factory.ProductFactory;
import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.repository.OrderItemRepository;
import com.example.shopapi.domain.repository.ProductRepository;
import com.example.shopapi.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            OrderItemRepository orderItemRepository
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderItemRepository = orderItemRepository;
    }

    public Product createProduct(String description, BigDecimal price, int stock, Long categoryId) {
        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return productRepository.save(ProductFactory.createProduct(description, price, stock, category));
    }

    public Product updateProduct(Long productId, String description, BigDecimal price, int stock) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (price.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        if (stock < 0) {
            throw new IllegalArgumentException("Stock cannot be negative.");
        }

        Product updatedProduct = ProductFactory.updateProduct(existingProduct, description, price, stock);

        return productRepository.save(updatedProduct);
    }

    public Product markProductAsUnavailable(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        boolean isInUse = orderItemRepository.existsByProductId(productId);
        if (isInUse) {
            throw new IllegalStateException("Product is part of an active order and cannot be removed.");
        }

        Product updatedProduct = ProductFactory.markAsUnavailable(product);

        return productRepository.save(updatedProduct);
    }

    public List<Product> getAllProducts() {

        return productRepository.findAll();
    }

    public Optional<Product> getProductById(Long id) {

        return productRepository.findById(id);
    }

    public List<Product> getProductsByCategory(Long categoryId) {

        return productRepository.findByCategoryId(categoryId);
    }

    public List<Product> getAllAvailableProducts() {

        return productRepository.findByAvailableTrue();
    }

    public List<Product> getLowStockProducts(int stockThreshold) {

        return productRepository.findByStockLessThan(stockThreshold);
    }
}