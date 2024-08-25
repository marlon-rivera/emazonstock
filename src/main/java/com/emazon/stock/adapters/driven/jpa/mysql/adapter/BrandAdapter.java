package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IBrandEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IBrandRepository;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.spi.IBrandPersistencePort;
import lombok.RequiredArgsConstructor;

import java.util.Optional;

@RequiredArgsConstructor
public class BrandAdapter implements IBrandPersistencePort {

    private final IBrandRepository brandRepository;
    private final IBrandEntityMapper mapper;

    @Override
    public void saveBrand(Brand brand) {
        brandRepository.save(mapper.toEntity(brand));
    }

    @Override
    public Optional<Brand> findCategoryByName(String name) {
        return mapper.toBrandOptional(brandRepository.findByName(name));
    }
}
