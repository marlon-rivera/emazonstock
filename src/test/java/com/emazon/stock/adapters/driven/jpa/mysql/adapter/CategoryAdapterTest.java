package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.ICategoryEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class CategoryAdapterTest {
    @Mock
    private ICategoryRepository repository;

    @Mock
    private ICategoryEntityMapper mapper;

    @InjectMocks
    private CategoryAdapter adapter;

    private Category category;
    private Category category2;
    private CategoryEntity categoryEntity;
    private CategoryEntity categoryEntity2;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        category = new Category(1L, "Electronics", "Description");
        category2 = new Category(2L, "ZElectronics", "Description");
        categoryEntity = new CategoryEntity(1L, "Electronics", "Description");
        categoryEntity2 = new CategoryEntity(2L, "ZElectronics", "Description");
    }

    @Test
    void saveCategoryShouldSaveCategory() {
        when(mapper.toEntity(category)).thenReturn(categoryEntity);

        adapter.saveCategory(category);

        verify(repository).save(categoryEntity);
    }

    @Test
    void findCategoryByNameShouldReturnCategoryWhenFound() {
        when(repository.findByName("Electronics")).thenReturn(Optional.of(categoryEntity));
        when(mapper.toCategoryOptional(Optional.of(categoryEntity))).thenReturn(Optional.of(category));

        Optional<Category> result = adapter.findCategoryByName("Electronics");

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void findCategoryByIdShouldReturnCategoryWhenFound() {
        when(repository.findById(1L)).thenReturn(Optional.of(categoryEntity));
        when(mapper.toCategoryOptional(Optional.of(categoryEntity))).thenReturn(Optional.of(category));

        Optional<Category> result = adapter.findCategoryById(1L);

        assertTrue(result.isPresent());
        assertEquals(category, result.get());
    }

    @Test
    void getAllCategories_ShouldReturnPaginatedCategoriesWhenOrderIsAsc() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CategoryEntity> page =  new PageImpl<>(List.of(categoryEntity, categoryEntity2), pageRequest, 2);
        when(repository.findAllByOrderByNameAsc(pageRequest)).thenReturn(page);
        when(mapper.toCategoryList(page.getContent())).thenReturn(List.of(category,category2));


        PaginationInfo<Category> result = adapter.getAllCategories(0, 10, "ASC");

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(category, result.getList().get(0));
    }

    @Test
    void getAllCategories_ShouldReturnPaginatedCategoriesWhenOrderIsDesc() {
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<CategoryEntity> page =  new PageImpl<>(List.of(categoryEntity2, categoryEntity), pageRequest, 2);
        when(repository.findAllByOrderByNameAsc(pageRequest)).thenReturn(page);
        when(mapper.toCategoryList(page.getContent())).thenReturn(List.of(category2,category));


        PaginationInfo<Category> result = adapter.getAllCategories(0, 10, "ASC");

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(category2, result.getList().get(0));
    }

}