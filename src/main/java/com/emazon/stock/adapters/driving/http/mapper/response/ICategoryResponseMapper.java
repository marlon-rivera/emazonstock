package com.emazon.stock.adapters.driving.http.mapper.response;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.dto.response.CategoryResponse;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.factory.Mappers;

import java.util.List;

@Mapper(componentModel = "spring")
public interface ICategoryResponseMapper {

    @Mapping(target = "name", source = "category.name")
    @Mapping(target = "description", source = "category.description")
    CategoryResponse toCategoryResponse(Category category);

    List<CategoryResponse> toListCategoryResponse(List<Category> categoryList);

    PaginationInfo<CategoryResponse> toPaginationInfo(PaginationInfo<Category> paginationInfo);

}
