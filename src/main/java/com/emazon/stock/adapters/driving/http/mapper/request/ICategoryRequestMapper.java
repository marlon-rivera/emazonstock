package com.emazon.stock.adapters.driving.http.mapper.request;

import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface ICategoryRequestMapper {

    @Mapping(target = "id", source = "categoryRequest.id")
    @Mapping(target = "name", source = "categoryRequest.name")
    @Mapping(target = "description", source = "categoryRequest.description")
    Category toCategory(CategoryRequest categoryRequest);

}