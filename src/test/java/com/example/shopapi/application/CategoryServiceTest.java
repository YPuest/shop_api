package com.example.shopapi.application;

import com.example.shopapi.domain.model.Category;
import com.example.shopapi.domain.repository.CategoryRepository;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class CategoryServiceTest {

    private final CategoryRepository categoryRepository = mock(CategoryRepository.class);
    private final CategoryService categoryService = new CategoryService(categoryRepository);

    @Test
    void createCategory_shouldCreateNewCategory() {
        String categoryName = "Headset";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.empty());
        when(categoryRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        Category category = categoryService.createCategory(categoryName);

        assertNotNull(category);
        assertEquals(categoryName, category.getName());
        verify(categoryRepository, times(1)).save(any());
    }

    @Test
    void createCategory_shouldThrowExceptionWhenDuplicate() {
        String categoryName = "Headset";
        when(categoryRepository.findByName(categoryName)).thenReturn(Optional.of(new Category(categoryName)));

        Exception exception = assertThrows(IllegalArgumentException.class, () -> categoryService.createCategory(categoryName));

        assertEquals("Category already exists", exception.getMessage());
    }
}