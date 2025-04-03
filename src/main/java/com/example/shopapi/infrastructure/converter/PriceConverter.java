package com.example.shopapi.infrastructure.converter;

import com.example.shopapi.domain.model.valueobject.Price;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.math.BigDecimal;

@Converter(autoApply = true)
public class PriceConverter implements AttributeConverter<Price, BigDecimal> {

    @Override
    public BigDecimal convertToDatabaseColumn(Price price) {
        return price != null ? price.getAmount() : null;
    }

    @Override
    public Price convertToEntityAttribute(BigDecimal dbData) {
        return dbData != null ? new Price(dbData) : null;
    }
}