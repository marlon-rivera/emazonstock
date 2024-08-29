package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.dto.response.CategoryResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.ICategoryRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.ICategoryResponseMapper;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
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

import java.util.List;
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

    @MockBean
    private ICategoryResponseMapper categoryResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void testSaveCategory() throws Exception {
        CategoryRequest categoryRequest = new CategoryRequest(1L, "Category", "Description for category");
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

        CategoryRequest categoryRequest = new CategoryRequest(1L ,longCategoryName, "Description for category");

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

        CategoryRequest categoryRequest = new CategoryRequest(1L, validCategoryName, longDescription);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION)));
    }

    @Test
    void testSaveCategoryWithEmptyNameShouldFail() throws Exception {
        String validDescription = "This is a valid description with less than 120 characters.";
        CategoryRequest categoryRequest = new CategoryRequest(1L, "", validDescription);

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

        CategoryRequest categoryRequest = new CategoryRequest(1L, validName, emptyDescription);

        mockMvc.perform(post("/category/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequest)))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_CATEGORY_DESCRIPTION_BLANK)));
    }

    @Test
    public void testGetAllCategories() throws Exception {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(categoryServicePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfoResponse<CategoryResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(categoryResponseMapper.toPaginationInfo(paginationInfo)).thenReturn(paginationInfoResponse.getPaginationInfo());

        mockMvc.perform(get("/category/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    public void testGetAllCategoriesWithIncorrectParamPageShouldFail() throws Exception {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(categoryServicePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfoResponse<CategoryResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(categoryResponseMapper.toPaginationInfo(paginationInfo)).thenReturn(paginationInfoResponse.getPaginationInfo());

        mockMvc.perform(get("/category/all")
                        .param("page", "-8")
                        .param("size", "10")
                        .param("order", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_MIN_VALUE_PAGE)));;
    }

    @Test
    public void testGetAllCategoriesWithIncorrectParamSizeShouldFail() throws Exception {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(categoryServicePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfoResponse<CategoryResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(categoryResponseMapper.toPaginationInfo(paginationInfo)).thenReturn(paginationInfoResponse.getPaginationInfo());

        mockMvc.perform(get("/category/all")
                        .param("page", "0")
                        .param("size", "0")
                        .param("order", "ASC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_MIN_VALUES_PER_PAGE)));;
    }

    @Test
    public void testGetAllCategoriesWithIncorrectParamOrderShouldFail() throws Exception {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(categoryServicePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfoResponse<CategoryResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(categoryResponseMapper.toPaginationInfo(paginationInfo)).thenReturn(paginationInfoResponse.getPaginationInfo());

        mockMvc.perform(get("/category/all")
                        .param("page", "0")
                        .param("size", "1")
                        .param("order", "ASCDESC")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_REGEX_ORDER)));;
    }
}