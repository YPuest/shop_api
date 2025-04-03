package com.example.shopapi.domain.model.valueobject;

import java.math.BigDecimal;
import java.util.Objects;

public class Price {
    private final BigDecimal amount;

    public Price(BigDecimal amount) {
        if (amount == null || amount.compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Price must be greater than zero.");
        }
        this.amount = amount;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Price add(Price other) {
        return new Price(this.amount.add(other.amount));
    }

    public Price subtract(Price other) {
        return new Price(this.amount.subtract(other.amount));
    }

    @Override
    public String toString() {
        return amount.toPlainString();
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Price)) return false;
        Price price = (Price) o;
        return amount.compareTo(price.amount) == 0;
    }

    @Override
    public int hashCode() {
        return Objects.hash(amount);
    }
}