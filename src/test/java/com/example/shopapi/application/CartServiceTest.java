package com.example.shopapi.application;

import com.example.shopapi.domain.model.*;
import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.model.valueobject.ProductDescription;
import com.example.shopapi.domain.model.valueobject.Stock;
import com.example.shopapi.domain.repository.*;
import com.example.shopapi.domain.service.ProductDomainService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CartServiceTest {

    private CartRepository cartRepository;
    private CustomerRepository customerRepository;
    private ProductRepository productRepository;
    private OrderRepository orderRepository;
    private OrderItemRepository orderItemRepository;
    private OrderStatusRepository orderStatusRepository;
    private ProductDomainService domainService;

    private CartService cartService;

    @BeforeEach
    void setUp() {
        cartRepository = mock(CartRepository.class);
        customerRepository = mock(CustomerRepository.class);
        productRepository = mock(ProductRepository.class);
        orderRepository = mock(OrderRepository.class);
        orderItemRepository = mock(OrderItemRepository.class);
        orderStatusRepository = mock(OrderStatusRepository.class);
        domainService = mock(ProductDomainService.class);

        cartService = new CartService(
                cartRepository,
                customerRepository,
                productRepository,
                orderRepository,
                orderItemRepository,
                orderStatusRepository,
                domainService
        );
    }

    @Test
    void addProductToCart_shouldAddNewProduct() {
        Long customerId = 1L;
        Long productId = 10L;
        int quantity = 2;

        Customer customer = new Customer("Max", "max@example.com");
        Product product = new Product(
                new ProductDescription("Gaming Laptop"),
                new Price(new BigDecimal("1499.99")),
                new Stock(10),
                new Category("Laptop")
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.empty());
        when(customerRepository.findById(customerId)).thenReturn(Optional.of(customer));
        when(cartRepository.save(any(Cart.class))).thenAnswer(inv -> inv.getArgument(0));

        cartService.addProductToCart(customerId, productId, quantity);

        verify(cartRepository, atLeastOnce()).save(any(Cart.class));
    }

    @Test
    void addProductToCart_shouldThrowIfProductNotAvailable() {
        Long customerId = 1L;
        Long productId = 10L;

        Product product = mock(Product.class);
        when(productRepository.findById(productId)).thenReturn(Optional.of(product));
        when(product.isAvailable()).thenReturn(false);

        Exception ex = assertThrows(IllegalStateException.class,
                () -> cartService.addProductToCart(customerId, productId, 1));

        assertEquals("Product is not available", ex.getMessage());
    }

    @Test
    void addProductToCart_shouldThrowIfNotEnoughStock() {
        Long customerId = 1L;
        Long productId = 10L;

        Product product = new Product(
                new ProductDescription("Gaming Mouse"),
                new Price(new BigDecimal("49.99")),
                new Stock(1),
                new Category("Mouse")
        );

        when(productRepository.findById(productId)).thenReturn(Optional.of(product));

        Exception ex = assertThrows(IllegalStateException.class,
                () -> cartService.addProductToCart(customerId, productId, 5));

        assertEquals("Not enough stock", ex.getMessage());
    }

    @Test
    void checkout_shouldCreateOrderAndClearCart() {
        Long customerId = 1L;

        Customer customer = new Customer("Max", "max@example.com");
        Product product = new Product(
                new ProductDescription("Keyboard"),
                new Price(new BigDecimal("79.99")),
                new Stock(10),
                new Category("Tastatur")
        );
        Cart cart = new Cart(customer);
        cart.addOrUpdateItem(product, 2);

        OrderStatus status = new OrderStatus("Pending");

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(orderStatusRepository.findByStatus("Pending")).thenReturn(Optional.of(status));
        when(orderRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));
        when(domainService.isAvailable(product)).thenReturn(true);

        cartService.checkout(customerId);

        verify(orderRepository, times(1)).save(any(Order.class));
        verify(orderItemRepository, times(1)).save(any(OrderItem.class));
        verify(productRepository, times(1)).save(any(Product.class));
        verify(cartRepository, times(1)).delete(cart);
    }

    @Test
    void checkout_shouldFailIfCartNotFound() {
        when(cartRepository.findByCustomerId(1L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalStateException.class, () -> cartService.checkout(1L));

        assertEquals("Cart not found", ex.getMessage());
    }

    @Test
    void removeProductFromCart_shouldRemoveProduct() {
        Long customerId = 1L;
        Long productId = 10L;

        Customer customer = new Customer("Max", "max@example.com");
        Product product = new Product(
                new ProductDescription("Gaming Monitor"),
                new Price(new BigDecimal("299.99")),
                new Stock(15),
                new Category("Monitor")
        );

        Cart cart = new Cart(customer);
        cart.addOrUpdateItem(product, 1);

        when(cartRepository.findByCustomerId(customerId)).thenReturn(Optional.of(cart));
        when(cartRepository.save(any())).thenAnswer(inv -> inv.getArgument(0));

        cartService.removeProductFromCart(customerId, productId);

        assertTrue(cart.getItems().isEmpty());
        verify(cartRepository, times(1)).save(cart);
    }

    @Test
    void removeProductFromCart_shouldThrowIfCartNotFound() {
        when(cartRepository.findByCustomerId(99L)).thenReturn(Optional.empty());

        Exception ex = assertThrows(IllegalStateException.class,
                () -> cartService.removeProductFromCart(99L, 10L));

        assertEquals("Cart not found", ex.getMessage());
    }
}