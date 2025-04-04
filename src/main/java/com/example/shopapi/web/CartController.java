package com.example.shopapi.web;

import com.example.shopapi.application.CartService;
import com.example.shopapi.domain.model.Cart;
import com.example.shopapi.domain.model.CartItem;
import com.example.shopapi.web.dto.CartAddRequest;
import com.example.shopapi.web.dto.CartItemResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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
    public ResponseEntity<String> checkout(@RequestParam Long customerId) {
        cartService.checkout(customerId);
        return ResponseEntity.ok("Checkout completed. Order has been created.");
    }

    @GetMapping
    public ResponseEntity<List<CartItemResponse>> getCart(@RequestParam Long customerId) {
        Cart cart = cartService.getCart(customerId);
        List<CartItemResponse> items = cart.getItems().stream()
                .map(item -> new CartItemResponse(
                        item.getProduct().getId(),
                        item.getProduct().getDescription().getValue(),
                        item.getProduct().getPrice().getAmount(),
                        item.getQuantity()))
                .collect(Collectors.toList());

        return ResponseEntity.ok(items);
    }
}