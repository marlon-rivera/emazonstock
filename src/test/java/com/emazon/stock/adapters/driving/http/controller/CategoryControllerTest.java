package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.mapper.request.ICategoryRequestMapper;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = CategoryController.class)
@ExtendWith(MockitoExtension.class)
public class CategoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ICategoryServicePort categoryServicePort;

    @MockBean
    private ICategoryRequestMapper categoryRequestMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveCategory() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest("Category", "Description for category");
        Category category = new Category(null, "Category", "Description for category");

        when(categoryRequestMapper.toCategory(any(CategoryRequest.class))).thenReturn(category);
        doNothing().when(categoryServicePort).saveCategory(any(Category.class));

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isCreated());

        verify(categoryRequestMapper).toCategory(any(CategoryRequest.class));
        verify(categoryServicePort).saveCategory(any(Category.class));
    }

    @Test
    void testSaveCategoryWithLongNameShouldFail() throws Exception {
        String longCategoryName = "ThisCategoryNameIsWayTooLongAndShouldCauseAValidationError";

        CategoryRequest categoryRequest = new CategoryRequest(longCategoryName, "Description for category");

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_NAME)));
    }

    @Test
    void testSaveCategoryWithLongDescriptionShouldFail() throws Exception {
        String validCategoryName = "Category";
        String longDescription = "This description is too long and should trigger a validation error because it exceeds the 120 characters limit that has been imposed on this field.";

        CategoryRequest categoryRequest = new CategoryRequest(validCategoryName, longDescription);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION)));
    }

    @Test
    void testSaveCategoryWithEmptyNameShouldFail() throws Exception {
        String validDescription = "This is a valid description with less than 120 characters.";
        CategoryRequest categoryRequest = new CategoryRequest("", validDescription);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_NAME_BLANK)));
    }

    @Test
    void testSaveCategoryWithEmptyDescriptionShouldFail() throws Exception {
        String validName = "ValidCategoryName";
        String emptyDescription = "";

        CategoryRequest categoryRequest = new CategoryRequest(validName, emptyDescription);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_DESCRIPTION_BLANK)));
    }
}