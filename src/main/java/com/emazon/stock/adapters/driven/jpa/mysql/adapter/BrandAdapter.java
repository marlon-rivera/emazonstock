package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IBrandEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IBrandRepository;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IBrandPersistencePort;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

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
    public Optional<Brand> findBrandByName(String name) {
        return mapper.toBrandOptional(brandRepository.findByName(name));
    }

    @Override
    public PaginationInfo<Brand> getAllBrands(int page, int size, String order){
        Pageable pagination = PageRequest.of(page, size);
        Page<BrandEntity> brands;
        if(order.equals(Constants.ORDER_ASC)){
            brands = brandRepository.findAllByOrderByNameAsc(pagination);
        }else{
            brands = brandRepository.findAllByOrderByNameDesc(pagination);
        }
        return new PaginationInfo<>(
                mapper.toBrandList(brands.getContent()),
                brands.getNumber(),
                brands.getSize(),
                brands.getTotalElements(),
                brands.getTotalPages(),
                brands.hasNext(),
                brands.hasPrevious()
        );
    }

    @Override
    public Optional<Brand> findBrandById(Long id) {
        return mapper.toBrandOptional(brandRepository.findById(id));
    }
}
