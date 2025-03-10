package com.example.shopapi.web;

import com.example.shopapi.application.ProductService;
import com.example.shopapi.domain.model.Product;
import com.example.shopapi.web.dto.ProductRequest;
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

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody ProductRequest productRequest) {
        Product product = productService.createProduct(
                productRequest.getDescription(),
                productRequest.getPrice(),
                productRequest.getStock(),
                productRequest.getCategoryId()
        );
        return ResponseEntity.ok(product);
    }

    @GetMapping
    public ResponseEntity<List<Product>> getAllProducts() {
        return ResponseEntity.ok(productService.getAllProducts());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Product> getProductById(@PathVariable Long id) {
        Optional<Product> product = productService.getProductById(id);
        return product.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @GetMapping("/category/{categoryId}")
    public ResponseEntity<List<Product>> getProductsByCategory(@PathVariable Long categoryId) {
        return ResponseEntity.ok(productService.getProductsByCategory(categoryId));
    }

    @GetMapping("/available")
    public List<Product> getAvailableProducts() {

        return productService.getAllProducts();
    }

    @GetMapping("/low-stock")
    public List<Product> getLowStockProducts(@RequestParam(defaultValue = "5") int threshold) {

        return productService.getLowStockProducts(threshold);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Product> updateProduct(
            @PathVariable Long id,
            @RequestBody ProductUpdateRequest productUpdateRequest) {

        Product updatedProduct = productService.updateProduct(
                id,
                productUpdateRequest.getDescription(),
                productUpdateRequest.getPrice(),
                productUpdateRequest.getStock()
        );
        return ResponseEntity.ok(updatedProduct);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> markProductAsUnavailable(@PathVariable Long id) {
        productService.markProductAsUnavailable(id);
        return ResponseEntity.ok("Product has been marked as unavailable.");
    }
}