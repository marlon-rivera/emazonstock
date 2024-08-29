package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;

import java.util.Optional;

public interface IBrandPersistencePort {

    void saveBrand(Brand brand);
    Optional<Brand> findCategoryByName(String name);
    PaginationInfo<Brand> getAllBrands(int page, int size, String order);
    Optional<Brand> findBrandById(Long id);

}
