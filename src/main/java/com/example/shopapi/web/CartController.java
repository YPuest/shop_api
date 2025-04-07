package com.example.shopapi.web;

import com.example.shopapi.application.CartService;
import com.example.shopapi.domain.model.Cart;
import com.example.shopapi.domain.model.CartItem;
import com.example.shopapi.web.dto.CartAddRequest;
import com.example.shopapi.web.dto.CartItemResponse;
import com.example.shopapi.web.dto.CartResponse;
import com.example.shopapi.web.dto.CheckoutResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/cart")
public class CartController {

    private final CartService cartService;

    public CartController(CartService cartService) {
        this.cartService = cartService;
    }

    @PostMapping("/add")
    public ResponseEntity<String> addToCart(@RequestBody CartAddRequest request) {
        cartService.addProductToCart(request.getCustomerId(), request.getProductId(), request.getQuantity());
        return ResponseEntity.ok("Product added to cart.");
    }

    @PostMapping("/checkout")
    public ResponseEntity<CheckoutResponse> checkout(@RequestParam Long customerId) {
        Long orderId = cartService.checkoutAndReturnOrderId(customerId);
        return ResponseEntity.ok(new CheckoutResponse(orderId, "Checkout completed. Order has been created."));
    }

    @GetMapping
    public ResponseEntity<CartResponse> getCart(@RequestParam Long customerId) {
        Cart cart = cartService.getCart(customerId);

        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getDescription().getValue(),
                        item.getProduct().getPrice().getAmount(),
                        item.getQuantity()))
                .toList();

        BigDecimal total = items.stream()
                .map(item -> item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity())))
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return ResponseEntity.ok(new CartResponse(items, total));
    }

    @DeleteMapping("/remove")
    public ResponseEntity<String> removeFromCart(@RequestParam Long customerId,
                                                 @RequestParam Long productId) {
        cartService.removeProductFromCart(customerId, productId);
        return ResponseEntity.ok("Product removed from cart.");
    }
}