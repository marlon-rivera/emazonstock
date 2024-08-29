package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.exception.category.*;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.ICategoryPersistencePort;
import com.emazon.stock.utils.Constants;

import java.util.Optional;

public class CategoryUseCaseImpl implements ICategoryServicePort {

    private final ICategoryPersistencePort persistencePort;

    public CategoryUseCaseImpl(ICategoryPersistencePort persistencePort){
        this.persistencePort = persistencePort;
    }

    @Override
    public void saveCategory(Category category) {
        category.setName(category.getName().trim());
        category.setDescription(category.getDescription().trim());
        if(persistencePort.findCategoryByName(category.getName()).isPresent()){
            throw new CategoryAlreadyExistsException();
        }
        if(category.getName().isBlank()){
            throw new CategoryNameBlankException();
        }
        if(category.getDescription().isBlank()){
            throw new CategoryDescriptionBlankException();
        }
        if(category.getName().length() > Constants.MAX_CHARACTERS_NAME_CATEGORY){
            throw new CategoryMaximumNumberCharactersNameException();
        }
        if(category.getDescription().length() > Constants.MAX_CHARACTERS_DESCRIPTION_CATEGORY){
            throw new CategoryMaximumNumberCharactersDescriptionException();
        }
        persistencePort.saveCategory(category);
    }

    @Override
    public PaginationInfo<Category> getAllCategories(int page, int size, String order) {
        PaginationInfo<Category> categories = persistencePort.getAllCategories(page, size, order);
        if(categories.getList().isEmpty()){
            throw  new CategoryNoDataFoundException();
        }
        return categories;
    }

    @Override
    public Optional<Category> getCategoryById(Long id) {
        return persistencePort.findCategoryById(id);
    }
}

