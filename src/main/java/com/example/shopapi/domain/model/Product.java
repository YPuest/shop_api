package com.example.shopapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;

@Getter
@Entity
@Table(name = "product")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private Category category;

    @Column(nullable = false)
    private String description;

    @Column(nullable = false)
    private BigDecimal price;

    @Setter
    @Column(nullable = false)
    private int stock;

    @Column(nullable = false)
    private boolean available = true;

    protected Product() {}

    public Product(String description, BigDecimal price, int stock, Category category) {
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.available = true;
    }

    public void markAsUnavailable() {
        this.available = false;
    }
}