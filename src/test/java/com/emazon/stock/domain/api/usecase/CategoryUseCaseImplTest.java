package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.exception.*;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.ICategoryPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Collections;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

public class CategoryUseCaseImplTest {

    @Mock
    private ICategoryPersistencePort persistencePort;

    @InjectMocks
    private CategoryUseCaseImpl categoryUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveCategorySuccessfully() {
        String validName = "NewCategory";
        String validDescription = "Valid description";
        Category category = new Category(1L, validName, validDescription);

        when(persistencePort.findCategoryByName(validName)).thenReturn(Optional.empty());

        categoryUseCase.saveCategory(category);

        verify(persistencePort, times(1)).saveCategory(any(Category.class));
    }

    @Test
    void testSaveCategoryWithLongNameShouldFail() {

        String longName = "ThisCategoryNameIsWayTooLongAndShouldCauseAValidationError";
        String description = "Valid description";
        Category category = new Category(1L, longName, description);

        assertThrows(CategoryMaximumNumberCharactersNameException.class, () -> categoryUseCase.saveCategory(category));
    }

    @Test
    void testSaveCategoryWithLongDescriptionShouldFail() {
        String name = "ValidName";
        String longDescription = "This description is way too long and should cause a validation error because it exceeds the maximum length allowed by the application rules.";
        Category category = new Category(1L, name, longDescription);

        assertThrows(CategoryMaximumNumberCharactersDescriptionException.class, () -> categoryUseCase.saveCategory(category));
    }

    @Test
    void testSaveCategoryWithEmptyNameShouldFail() {
        String emptyName = "";
        String description = "Valid description";
        Category category = new Category(1L, emptyName, description);

        assertThrows(CategoryNameBlankException.class, () -> categoryUseCase.saveCategory(category));
    }

    @Test
    void testSaveCategoryWithEmptyDescriptionShouldFail() {
        String name = "ValidName";
        String emptyDescription = "";
        Category category = new Category(1L, name, emptyDescription);

        assertThrows(CategoryDescriptionBlankException.class, () -> categoryUseCase.saveCategory(category));
    }

    @Test
    void testSaveCategoryThatAlreadyExistsShouldFail() {
        String name = "ExistingCategory";
        String description = "Valid description";
        Category category = new Category(1L, name, description);

        when(persistencePort.findCategoryByName(name)).thenReturn(Optional.of(category));

        assertThrows(CategoryAlreadyExistsException.class, () -> categoryUseCase.saveCategory(category));
    }

    @Test
    public void testGetAllCategoriesSuccess() {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                Collections.singletonList(new Category(1L,"Category1", "Description1")),
                0,
                10,
                1,
                1,
                false,
                false
        );
        when(persistencePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfo<Category> result = categoryUseCase.getAllCategories(0, 10, "ASC");

        assertNotNull(result);
        assertFalse(result.getList().isEmpty());
        assertEquals("Category1", result.getList().get(0).getName());
    }

    @Test
    public void testGetAllCategoriesNoData() {
        PaginationInfo<Category> paginationInfo = new PaginationInfo<>(
                Collections.emptyList(),
                0,
                10,
                1,
                1,
                false,
                false
        );
        when(persistencePort.getAllCategories(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        assertThrows(CategoryNoDataFoundException.class, () -> {
            categoryUseCase.getAllCategories(0, 10, "ASC");
        });
    }

}