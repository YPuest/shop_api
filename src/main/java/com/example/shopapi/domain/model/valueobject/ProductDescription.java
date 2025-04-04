package com.example.shopapi.domain.model.valueobject;

import java.util.Objects;

public class ProductDescription {

    private final String value;

    protected ProductDescription() {
        this.value = null;
    }

    public ProductDescription(String value) {
        if (value == null || value.isBlank()) {
            throw new IllegalArgumentException("Description must not be null or empty.");
        }
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    @Override
    public String toString() {
        return value;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof ProductDescription that)) return false;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }
}