package com.example.shopapi.application;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.repository.ProductRepository;
import com.example.shopapi.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSucceed() {
        Category category = new Category("Computer");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product product = new Product("Powerful gaming computer", new BigDecimal("1499.99"), 10, category);
        when(productRepository.save(any(Product.class))).thenReturn(product);

        Product createdProduct = productService.createProduct("Powerful gaming computer", new BigDecimal("1499.99"), 10, 1L);

        assertNotNull(createdProduct);
        verify(productRepository, times(1)).save(any(Product.class));
    }

    @Test
    void createProduct_shouldFailIfCategoryNotFound() {
        when(categoryRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                productService.createProduct("Powerful gaming computer", new BigDecimal("1499.99"), 10, 999L));

        assertEquals("Category not found", exception.getMessage());
    }
}