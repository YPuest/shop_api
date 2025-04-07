package com.example.shopapi.domain.model;

import jakarta.persistence.*;
import lombok.Getter;

import com.example.shopapi.domain.model.valueobject.Price;
import com.example.shopapi.domain.model.valueobject.Stock;
import com.example.shopapi.domain.model.valueobject.ProductDescription;
import lombok.Setter;

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
    @Setter
    private ProductDescription description;

    @Column(nullable = false)
    private Price price;

    @Column(nullable = false)
    @Setter
    private Stock stock;

    @Column(nullable = false)
    private boolean available = true;

    protected Product() {}

    public Product(ProductDescription description, Price price, Stock stock, Category category) {
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.category = category;
        this.available = true;
    }

    public boolean hasStock(int quantity) {
        return stock.hasEnough(quantity);
    }

    public void decreaseStock(int quantity) {
        stock.decrease(quantity);
    }

    public void markAsUnavailable() {
        this.available = false;
    }
}