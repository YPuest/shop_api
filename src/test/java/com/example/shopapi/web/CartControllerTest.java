package com.example.shopapi.web;

import com.example.shopapi.application.CartService;
import com.example.shopapi.domain.model.*;
import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.model.valueobject.ProductDescription;
import com.example.shopapi.domain.model.valueobject.Stock;
import com.example.shopapi.web.dto.CartAddRequest;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import org.springframework.beans.factory.annotation.Autowired;

import java.math.BigDecimal;
import java.util.Collections;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CartController.class)
@Import(CartControllerTest.MockConfig.class)
class CartControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private CartService cartService;

    @Autowired
    private ObjectMapper objectMapper;

    @TestConfiguration
    static class MockConfig {
        @Bean
        public CartService cartService() {
            return mock(CartService.class);
        }
    }

    @Test
    void addToCart_shouldReturnOk() throws Exception {
        CartAddRequest request = new CartAddRequest(1L, 10L, 2);

        mockMvc.perform(post("/cart/add")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(content().string("Product added to cart."));

        verify(cartService, times(1)).addProductToCart(1L, 10L, 2);
    }

    @Test
    void getCart_shouldReturnCartItems() throws Exception {
        Product product = new Product(
                new ProductDescription("Maus"),
                new Price(new BigDecimal("49.99")),
                new Stock(50),
                new Category("Maus")
        );
        Cart cart = new Cart(new Customer("Max", "max@example.com"));
        cart.addOrUpdateItem(product, 2);

        when(cartService.getCart(1L)).thenReturn(cart);

        mockMvc.perform(get("/cart")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].productId").value(product.getId()))
                .andExpect(jsonPath("$[0].description").value("Maus"))
                .andExpect(jsonPath("$[0].price").value(49.99))
                .andExpect(jsonPath("$[0].quantity").value(2));
    }

    @Test
    void checkout_shouldReturnConfirmation() throws Exception {
        mockMvc.perform(post("/cart/checkout")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Checkout completed. Order has been created."));

        verify(cartService, times(1)).checkout(1L);
    }

    @Test
    void removeFromCart_shouldReturnOk() throws Exception {
        mockMvc.perform(delete("/cart/remove")
                        .param("customerId", "1")
                        .param("productId", "10"))
                .andExpect(status().isOk())
                .andExpect(content().string("Product removed from cart."));

        verify(cartService, times(1)).removeProductFromCart(1L, 10L);
    }

    @Test
    void getCart_shouldReturnItemsWithTotal() throws Exception {
        Product product = new Product(
                new ProductDescription("Tastatur"),
                new Price(new BigDecimal("79.99")),
                new Stock(30),
                new Category("Tastatur")
        );
        Customer customer = new Customer("Erika", "erika@example.com");
        Cart cart = new Cart(customer);
        cart.addOrUpdateItem(product, 2);

        when(cartService.getCart(1L)).thenReturn(cart);

        mockMvc.perform(get("/cart")
                        .param("customerId", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.items[0].productId").value(product.getId()))
                .andExpect(jsonPath("$.items[0].description").value("Tastatur"))
                .andExpect(jsonPath("$.items[0].price").value(79.99))
                .andExpect(jsonPath("$.items[0].quantity").value(2))
                .andExpect(jsonPath("$.total").value(159.98));
    }
}