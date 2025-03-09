package com.example.shopapi.domain.model;

import jakarta.persistence.*;
import java.math.BigDecimal;

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

    @Column(nullable = false)
    private int stock;

    protected Product() {}

    public Product(String description, BigDecimal price, int stock, Category category) {
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
    }

    public Long getId() { return id; }
    public Category getCategory() { return category; }
    public String getDescription() { return description; }
    public BigDecimal getPrice() { return price; }
    public int getStock() { return stock; }
}