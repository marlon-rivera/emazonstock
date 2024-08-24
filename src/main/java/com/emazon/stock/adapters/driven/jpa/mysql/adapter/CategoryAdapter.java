package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.mapper.ICategoryEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.spi.ICategoryPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class CategoryAdapter implements ICategoryPersistencePort {
    private final ICategoryRepository repository;
    private final ICategoryEntityMapper mapper;

    @Override
    public void saveCategory(Category category) {
        repository.save(mapper.toEntity(category));
    }

    @Override
    public Optional<Category> findCategoryByName(String name) {
        return mapper.toCategoryOptional(repository.findByName(name));
    }


}
