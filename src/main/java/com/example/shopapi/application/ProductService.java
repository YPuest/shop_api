package com.example.shopapi.application;

import com.example.shopapi.domain.factory.ProductFactory;
import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.repository.OrderItemRepository;
import com.example.shopapi.domain.repository.ProductRepository;
import com.example.shopapi.domain.repository.CategoryRepository;
import com.example.shopapi.domain.service.ProductDomainService;
import com.example.shopapi.domain.service.ProductValidator;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@Service
public class ProductService {

    private final ProductRepository productRepository;
    private final CategoryRepository categoryRepository;
    private final OrderItemRepository orderItemRepository;
    private final ProductValidator validator;
    private final ProductDomainService domainService;

    public ProductService(
            ProductRepository productRepository,
            CategoryRepository categoryRepository,
            OrderItemRepository orderItemRepository,
            ProductValidator validator,
            ProductDomainService domainService
    ) {
        this.productRepository = productRepository;
        this.categoryRepository = categoryRepository;
        this.orderItemRepository = orderItemRepository;
        this.validator = validator;
        this.domainService = domainService;
    }

    public Product createProduct(String description, Price price, int stock, Long categoryId) {
        validator.validate(description, stock);

        Category category = categoryRepository.findById(categoryId)
                .orElseThrow(() -> new IllegalArgumentException("Category not found"));

        return productRepository.save(ProductFactory.createProduct(description, price, stock, category));
    }

    public Product updateProduct(Long productId, String description, Price price, int stock) {
        Product existingProduct = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        validator.validate(description, stock);

        Product updatedProduct = ProductFactory.updateProduct(existingProduct, description, price, stock);

        return productRepository.save(updatedProduct);
    }

    public Product markProductAsUnavailable(Long productId) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        domainService.ensureProductCanBeMarkedAsUnavailable(productId);

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