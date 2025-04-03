package com.example.shopapi.domain.model.valueobject;

import java.util.Objects;

public class Stock {

    private int quantity;

    public Stock(int quantity) {
        if (quantity < 0) {
            throw new IllegalArgumentException("Stock must not be negative.");
        }
        this.quantity = quantity;
    }

    public int getQuantity() {
        return quantity;
    }

    public void increase(int amount) {
        if (amount < 0) throw new IllegalArgumentException("Increase amount must be positive.");
        this.quantity += amount;
    }

    public void decrease(int amount) {
        if (amount < 0 || amount > quantity) {
            throw new IllegalArgumentException("Not enough stock.");
        }
        this.quantity -= amount;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Stock)) return false;
        Stock stock = (Stock) o;
        return quantity == stock.quantity;
    }

    @Override
    public int hashCode() {
        return Objects.hash(quantity);
    }

    @Override
    public String toString() {
        return String.valueOf(quantity);
    }

    public boolean hasEnough(int amount) {
        return quantity >= amount;
    }
}