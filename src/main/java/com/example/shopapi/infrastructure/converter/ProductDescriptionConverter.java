package com.example.shopapi.infrastructure.converter;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import com.example.shopapi.domain.model.valueobject.ProductDescription;

@Converter(autoApply = true)
public class ProductDescriptionConverter implements AttributeConverter<ProductDescription, String> {

    @Override
    public String convertToDatabaseColumn(ProductDescription description) {
        return description != null ? description.getValue() : null;
    }

    @Override
    public ProductDescription convertToEntityAttribute(String dbData) {
        return dbData != null ? new ProductDescription(dbData) : null;
    }
}