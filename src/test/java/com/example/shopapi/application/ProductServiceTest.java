package com.example.shopapi.application;
import com.example.shopapi.domain.factory.ProductFactory;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.repository.OrderItemRepository;
import com.example.shopapi.domain.repository.ProductRepository;
import com.example.shopapi.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@org.junit.jupiter.api.extension.ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    private ProductRepository productRepository;

    @Mock
    private CategoryRepository categoryRepository;

    @Mock
    private OrderItemRepository orderItemRepository;

    @InjectMocks
    private ProductService productService;

    @Test
    void createProduct_shouldSucceed() {
        Category category = new Category("Computer");
        when(categoryRepository.findById(1L)).thenReturn(Optional.of(category));

        Product product = ProductFactory.createProduct("Powerful gaming computer", new BigDecimal("1499.99"), 10, category);
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

    @Test
    void updateProduct_shouldSucceed() {
        Category category = new Category("Computer");
        Product existingProduct = new Product("Old description", new BigDecimal("100.00"), 5, category);
        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.updateProduct(1L, "New description", new BigDecimal("120.00"), 10);

        assertNotNull(updatedProduct);
        assertEquals("New description", updatedProduct.getDescription());
        assertEquals(new BigDecimal("120.00"), updatedProduct.getPrice());
        assertEquals(10, updatedProduct.getStock());
    }

    @Test
    void updateProduct_shouldFailIfProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(999L, "New description", new BigDecimal("120.00"), 10));

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void updateProduct_shouldFailIfPriceIsZeroOrNegative() {
        Category category = new Category("Computer");
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product("Old description", new BigDecimal("100.00"), 5, category)));

        Exception zeroException = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(1L, "New description", new BigDecimal("0.00"), 10));

        Exception negativeException = assertThrows(IllegalArgumentException.class, () ->
                productService.updateProduct(1L, "New description", new BigDecimal("-10.00"), 10));

        assertEquals("Price must be greater than zero.", zeroException.getMessage());
        assertEquals("Price must be greater than zero.", negativeException.getMessage());
    }

    @Test
    void markProductAsUnavailable_shouldSucceed() {
        Category category = new Category("Computer");
        Product existingProduct = ProductFactory.createProduct("Old description", new BigDecimal("100.00"), 5, category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(orderItemRepository.existsByProductId(1L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.markProductAsUnavailable(1L);

        assertNotNull(updatedProduct);
        assertFalse(updatedProduct.isAvailable());
    }

    @Test
    void markProductAsUnavailable_shouldFailIfProductNotFound() {
        when(productRepository.findById(999L)).thenReturn(Optional.empty());

        Exception exception = assertThrows(IllegalArgumentException.class, () ->
                productService.markProductAsUnavailable(999L));

        assertEquals("Product not found", exception.getMessage());
    }

    @Test
    void markProductAsUnavailable_shouldFailIfProductIsInUse() {
        when(productRepository.findById(1L)).thenReturn(Optional.of(new Product("Description", new BigDecimal("100.00"), 5, new Category("Computer"))));
        when(orderItemRepository.existsByProductId(1L)).thenReturn(true);

        Exception exception = assertThrows(IllegalStateException.class, () ->
                productService.markProductAsUnavailable(1L));

        assertEquals("Product is part of an active order and cannot be removed.", exception.getMessage());
    }

    @Test
    void markProductAsUnavailable_shouldSetAvailableFalse() {
        Category category = new Category("Computer");
        Product existingProduct = new Product("Old description", new BigDecimal("100.00"), 5, category);

        when(productRepository.findById(1L)).thenReturn(Optional.of(existingProduct));
        when(orderItemRepository.existsByProductId(1L)).thenReturn(false);
        when(productRepository.save(any(Product.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Product updatedProduct = productService.markProductAsUnavailable(1L);

        assertFalse(updatedProduct.isAvailable(), "Product should be marked as unavailable");
    }

    @Test
    void getAllAvailableProducts_shouldReturnOnlyAvailableProducts() {
        List<Product> availableProducts = List.of(
                new Product("Gaming Laptop", new BigDecimal("1499.99"), 10, new Category("Laptop")),
                new Product("Wireless Mouse", new BigDecimal("49.99"), 30, new Category("Mouse"))
        );

        when(productRepository.findByAvailableTrue()).thenReturn(availableProducts);

        List<Product> result = productService.getAllAvailableProducts();

        assertEquals(2, result.size());
    }

    @Test
    void getLowStockProducts_shouldReturnProductsWithStockBelowThreshold() {
        List<Product> lowStockProducts = List.of(
                new Product("Mechanical Keyboard", new BigDecimal("79.99"), 3, new Category("Keyboard"))
        );

        when(productRepository.findByStockLessThan(5)).thenReturn(lowStockProducts);

        List<Product> result = productService.getLowStockProducts(5);

        assertEquals(1, result.size());
        assertEquals("Mechanical Keyboard", result.get(0).getDescription());
    }
}