package com.example.shopapi.web;

import com.example.shopapi.application.ProductService;
import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.web.dto.ProductUpdateRequest;
import com.example.shopapi.web.exception.GlobalExceptionHandler;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.math.BigDecimal;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class ProductControllerTest {

    private MockMvc mockMvc;

    @Mock
    private ProductService productService;

    @InjectMocks
    private ProductController productController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders
                .standaloneSetup(productController)
                .setControllerAdvice(new GlobalExceptionHandler())
                .build();
    }

    @Test
    void createProduct_shouldReturnBadRequestForInvalidJson() throws Exception {
        String invalidJson = "{ \"description\": \"Laptop\", \"price\": \"invalid\" }";

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(invalidJson))
                .andExpect(status().isBadRequest());
    }

    @Test
    void updateProduct_shouldReturnUpdatedProduct() throws Exception {
        ProductUpdateRequest request = new ProductUpdateRequest("Updated Description", new BigDecimal("149.99"), 10);
        Product updatedProduct = new Product("Updated Description", new BigDecimal("149.99"), 10, new Category("name"));

        when(productService.updateProduct(eq(1L), anyString(), any(), anyInt())).thenReturn(updatedProduct);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(new ObjectMapper().writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.description").value("Updated Description"))
                .andExpect(jsonPath("$.price").value(149.99))
                .andExpect(jsonPath("$.stock").value(10));
    }

    @Test
    void markProductAsUnavailable_shouldReturnSuccess() throws Exception {
        when(productService.markProductAsUnavailable(1L)).thenReturn(new Product("Description", new BigDecimal("100.00"), 5, new Category("Laptop")));

        mockMvc.perform(delete("/products/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product has been marked as unavailable."));
    }

    @Test
    void markProductAsUnavailable_shouldReturnErrorIfProductNotFound() throws Exception {
        doThrow(new IllegalArgumentException("Product not found"))
                .when(productService)
                .markProductAsUnavailable(eq(999L));

        mockMvc.perform(delete("/products/999"))
                .andExpect(status().isBadRequest())  // Expect HTTP 400 due to GlobalExceptionHandler
                .andExpect(content().string("Product not found"));

        verify(productService, times(1)).markProductAsUnavailable(eq(999L));
    }
}