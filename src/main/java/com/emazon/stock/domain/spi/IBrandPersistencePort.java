package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Brand;

import java.util.Optional;

public interface IBrandPersistencePort {

    void saveBrand(Brand brand);
    Optional<Brand> findCategoryByName(String name);

}
