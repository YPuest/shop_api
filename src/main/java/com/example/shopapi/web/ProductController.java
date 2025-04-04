package com.example.shopapi.web;

import com.example.shopapi.application.ProductService;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.model.valueobject.ProductDescription;
import com.example.shopapi.domain.model.valueobject.Stock;
import com.example.shopapi.web.dto.ProductRequest;
import com.example.shopapi.web.dto.ProductResponse;
import com.example.shopapi.web.dto.ProductUpdateRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductService productService;

    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    private ProductResponse mapToResponse(Product product) {
        return new ProductResponse(
            product.getId(),
            product.getDescription().getValue(),
            product.getPrice().getAmount(),
            product.getStock().getQuantity(),
            product.isAvailable()
        );
    }

    @PostMapping
    public ResponseEntity<ProductResponse> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(
                new ProductDescription(productRequest.getDescription()),
                productRequest.getPrice(),
                new Stock(productRequest.getStock()),
                productRequest.getCategoryId()
        );
        return ResponseEntity.ok(mapToResponse(product));
    }

    @GetMapping
    public ResponseEntity<List<ProductResponse>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts()
                .stream()
                .map(this::mapToResponse)
                .toList());
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductResponse> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(p -> ResponseEntity.ok(mapToResponse(p)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<ProductResponse>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId)
                .stream()
                .map(this::mapToResponse)
                .toList());
    }

    @GetMapping("/available")
    public List<ProductResponse> getAvailableProducts() {
        return productService.getAllProducts().stream()
                .map(this::mapToResponse)
                .toList();
    }

    @GetMapping("/low-stock")
    public List<ProductResponse> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {
        return productService.getLowStockProducts(threshold).stream()
                .map(this::mapToResponse)
                .toList();
    }

    @PutMapping("/{id}")
    public ResponseEntity<ProductResponse> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest productUpdateRequest) {

        Product updatedProduct = productService.updateProduct(
                id,
                new ProductDescription(productUpdateRequest.getDescription()),
                productUpdateRequest.getPrice(),
                new Stock(productUpdateRequest.getStock())
        );

        ProductResponse response = new ProductResponse(
                updatedProduct.getId(),
                updatedProduct.getDescription().getValue(),
                updatedProduct.getPrice().getAmount(),
                updatedProduct.getStock().getQuantity(),
                updatedProduct.isAvailable()
        );

        return ResponseEntity.ok(response);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> markProductAsUnavailable(@PathVariable Long id) {
        productService.markProductAsUnavailable(id);
        return ResponseEntity.ok("Product has been marked as unavailable.");
    }
}