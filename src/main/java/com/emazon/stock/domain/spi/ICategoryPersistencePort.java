package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Category;

import java.util.Optional;

public interface ICategoryPersistencePort {
    void saveCategory(Category category);
    Optional<Category> findCategoryByName(String name);
}
