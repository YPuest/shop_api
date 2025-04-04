package com.example.shopapi.application;

import com.example.shopapi.domain.model.*;
import com.example.shopapi.domain.repository.*;
import com.example.shopapi.domain.service.ProductDomainService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
public class CartService {

    private final CartRepository cartRepository;
    private final CustomerRepository customerRepository;
    private final ProductRepository productRepository;
    private final OrderRepository orderRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderStatusRepository orderStatusRepository;
    private final ProductDomainService domainService;

    public CartService(CartRepository cartRepository,
                       CustomerRepository customerRepository,
                       ProductRepository productRepository,
                       OrderRepository orderRepository,
                       OrderItemRepository orderItemRepository,
                       OrderStatusRepository orderStatusRepository,
                       ProductDomainService domainService) {
        this.cartRepository = cartRepository;
        this.customerRepository = customerRepository;
        this.productRepository = productRepository;
        this.orderRepository = orderRepository;
        this.orderItemRepository = orderItemRepository;
        this.orderStatusRepository = orderStatusRepository;
        this.domainService = domainService;
    }

    @Transactional
    public void addProductToCart(Long customerId, Long productId, int quantity) {
        Product product = productRepository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        if (!product.isAvailable()) {
            throw new IllegalStateException("Product is not available");
        }

        if (!product.getStock().hasEnough(quantity)) {
            throw new IllegalStateException("Not enough stock");
        }

        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseGet(() -> {
                    Customer customer = customerRepository.findById(customerId)
                            .orElseThrow(() -> new IllegalArgumentException("Customer not found"));
                    return cartRepository.save(new Cart(customer));
                });

        cart.addOrUpdateItem(product, quantity);
        cartRepository.save(cart);
    }

    @Transactional
    public void checkout(Long customerId) {
        Cart cart = cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalStateException("Cart not found"));

        OrderStatus status = orderStatusRepository.findByStatus("Pending")
                .orElseThrow(() -> new IllegalStateException("Order status not found"));

        Order order = new Order(cart.getCustomer(), status);
        order = orderRepository.save(order);

        for (CartItem item : cart.getItems()) {
            Product product = item.getProduct();

            if (!domainService.isAvailable(product)) {
                throw new IllegalStateException("Product '" + product.getDescription().getValue() + "' not available");
            }

            domainService.reduceStock(product, item.getQuantity());
            productRepository.save(product);

            OrderItem orderItem = new OrderItem(order, product, item.getQuantity());
            orderItemRepository.save(orderItem);
        }

        cartRepository.delete(cart);
    }

    public Cart getCart(Long customerId) {
        return cartRepository.findByCustomerId(customerId)
                .orElseThrow(() -> new IllegalArgumentException("Cart not found for customer"));
    }
}