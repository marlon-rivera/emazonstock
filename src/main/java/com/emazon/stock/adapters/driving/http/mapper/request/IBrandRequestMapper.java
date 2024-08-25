package com.emazon.stock.adapters.driving.http.mapper.request;

import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.domain.model.Brand;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IBrandRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "brandRequest.name")
    @Mapping(target = "description", source = "brandRequest.description")
    Brand toBrand(BrandRequest brandRequest);

}
