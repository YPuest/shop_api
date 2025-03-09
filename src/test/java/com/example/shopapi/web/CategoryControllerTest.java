package com.example.shopapi.web;

import com.example.shopapi.application.CategoryService;
import com.example.shopapi.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class CategoryControllerTest {

    private MockMvc mockMvc;

    @Mock
    private CategoryService categoryService;

    @InjectMocks
    private CategoryController categoryController;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(categoryController).build();
    }

    @Test
    void createCategory_shouldReturnCategory() throws Exception {
        String categoryName = "Laptop";
        Category category = new Category(categoryName);

        when(categoryService.createCategory(categoryName)).thenReturn(category);

        mockMvc.perform(post("/categories")
                        .param("name", categoryName)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value(categoryName));
    }

    @Test
    void getAllCategories_shouldReturnList() throws Exception {
        List<Category> categories = List.of(new Category("Laptop"), new Category("Mouse"));
        when(categoryService.getAllCategories()).thenReturn(categories);

        mockMvc.perform(get("/categories")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(categories.size()));
    }
}