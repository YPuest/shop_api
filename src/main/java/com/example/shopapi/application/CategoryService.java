package com.example.shopapi.application;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.repository.CategoryRepository;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class CategoryService {

    private final CategoryRepository categoryRepository;

    public CategoryService(CategoryRepository categoryRepository) {
        this.categoryRepository = categoryRepository;
    }

    public Category createCategory(String name) {
        if (categoryRepository.findByName(name).isPresent()) {
            throw new IllegalArgumentException("Category already exists");
        }
        return categoryRepository.save(new Category(name));
    }

    public List<Category> getAllCategories() {
        return categoryRepository.findAll();
    }
}