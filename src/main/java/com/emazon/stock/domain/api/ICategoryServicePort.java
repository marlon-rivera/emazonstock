package com.emazon.stock.domain.api;

import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;

import java.util.Optional;

public interface ICategoryServicePort {

    void saveCategory(Category category);
    PaginationInfo<Category> getAllCategories(int page, int size, String order);
    Optional<Category> getCategoryById(Long id);

}
