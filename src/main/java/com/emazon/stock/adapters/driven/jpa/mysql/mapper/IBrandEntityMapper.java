package com.emazon.stock.adapters.driven.jpa.mysql.mapper;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.Category;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface IBrandEntityMapper {

    BrandEntity toEntity(Brand brand);

    @Mapping(target = "id", ignore = true)
    Brand toBrand(BrandEntity brandEntity);

    default Optional<Brand> toBrandOptional(Optional<BrandEntity> brandEntityOptional) {
        return brandEntityOptional.map(this::toBrand);
    }

    List<Brand> toBrandList(List<BrandEntity> brandEntityList);

}
