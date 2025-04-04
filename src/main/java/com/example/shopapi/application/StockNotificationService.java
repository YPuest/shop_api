package com.example.shopapi.application;

import com.example.shopapi.domain.model.Product;
import com.example.shopapi.domain.repository.ProductRepository;
import com.example.shopapi.infrastructure.EmailService;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class StockNotificationService {

    private final ProductRepository productRepository;
    private final EmailService emailService;

    public StockNotificationService(ProductRepository productRepository, EmailService emailService) {
        this.productRepository = productRepository;
        this.emailService = emailService;
    }

    @Scheduled(cron = "0 0 * * * ?")
    public void checkLowStock() {
        List<Product> lowStockProducts = productRepository.findByStockLessThan(5);
        if (!lowStockProducts.isEmpty()) {
            String RED = "\u001B[31m";
            String YELLOW = "\u001B[33m";
            String RESET = "\u001B[0m";

            System.out.println(RED + "LOW STOCK WARNING!" + RESET);
            for (Product product : lowStockProducts) {
                System.out.println(RED + "Product: " + YELLOW + product.getDescription().getValue() + RED + " - Only " + product.getStock().getQuantity() + " units left!" + RESET);

                emailService.sendLowStockAlert("admin@example.com", product.getDescription().getValue(), product.getStock().getQuantity());
            }
        }
    }
}