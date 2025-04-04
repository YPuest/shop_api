package com.example.shopapi.infrastructure.converter;

import com.example.shopapi.domain.model.valueobject.Stock;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class StockConverter implements AttributeConverter<Stock, Integer> {

    @Override
    public Integer convertToDatabaseColumn(Stock stock) {
        return stock != null ? stock.getQuantity() : null;
    }

    @Override
    public Stock convertToEntityAttribute(Integer dbData) {
        return dbData != null ? new Stock(dbData) : null;
    }
}