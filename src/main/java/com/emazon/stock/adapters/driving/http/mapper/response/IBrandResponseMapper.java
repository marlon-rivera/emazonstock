package com.emazon.stock.adapters.driving.http.mapper.response;

import com.emazon.stock.adapters.driving.http.dto.response.BrandResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IBrandResponseMapper {

    @Mapping(target = "name", source = "brand.name")
    @Mapping(target = "description", source = "brand.description")
    BrandResponse toBrandResponse(Brand brand);

    List<BrandResponse> toListBrandResponse(List<Brand> brands);

    PaginationInfoResponse<BrandResponse> toPaginationInfoResponse(PaginationInfo<Brand> paginationInfo);

}
