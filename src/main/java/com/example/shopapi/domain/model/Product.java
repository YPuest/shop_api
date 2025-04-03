package com.example.shopapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.model.valueobject.Stock;
import com.example.shopapi.infrastructure.converter.PriceConverter;
import com.example.shopapi.infrastructure.converter.StockConverter;

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
    @Convert(converter = PriceConverter.class)
    private Price price;

    @Column(nullable = false)
    @Convert(converter = StockConverter.class)
    private Stock stock;

    @Column(nullable = false)
    private boolean available = true;

    protected Product() {}

    public Product(String description, Price price, Stock stock, Category category) {
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.available = true;
    }

    public void markAsUnavailable() {
        this.available = false;
    }

    public Stock getStock() {
        return stock;
    }

    public void setStock(Stock stock) {
        this.stock = stock;
    }
}