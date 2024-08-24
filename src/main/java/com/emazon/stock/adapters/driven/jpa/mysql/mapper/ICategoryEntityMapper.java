package com.emazon.stock.adapters.driven.jpa.mysql.mapper;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface ICategoryEntityMapper {

    CategoryEntity toEntity(Category category);

    @Mapping(target = "id", ignore = true)
    Category toCategory(CategoryEntity categoryEntity);

    default Optional<Category> toCategoryOptional(Optional<CategoryEntity> categoryEntityOptional) {
        return categoryEntityOptional.map(this::toCategory);
    }

    List<Category> toCategoryList(List<CategoryEntity> categoryListEntity);

}
