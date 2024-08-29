package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IBrandEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IBrandRepository;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

class BrandAdapterTest {

    @Mock
    private IBrandRepository brandRepository;

    @Mock
    private IBrandEntityMapper mapper;

    @InjectMocks
    private BrandAdapter brandAdapter;

    private Brand brand;
    private Brand brandTwo;
    private BrandEntity brandEntity;
    private BrandEntity brandEntityTwo;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        brand = new Brand(1L, "brand" , "description");
        brandEntity = new BrandEntity(1L, "brand" , "description");
        brandTwo = new Brand(2L, "cbrand" , "cdescription");
        brandEntityTwo = new BrandEntity(2L, "cbrand" , "cdescription");
    }

    @Test
    void saveBrandShouldSaveBrand() {
        when(mapper.toEntity(brand)).thenReturn(brandEntity);

        brandAdapter.saveBrand(brand);

        verify(brandRepository).save(brandEntity);
    }

    @Test
    void findBrandByNameShouldReturnBrandWhenFound(){
        when(brandRepository.findByName("brand")).thenReturn(Optional.of(brandEntity));
        when(mapper.toBrandOptional(Optional.of(brandEntity))).thenReturn(Optional.of(brand));

        Optional<Brand> result = brandAdapter.findBrandByName("brand");

        assertTrue(result.isPresent());
        assertEquals(brand, result.get());
    }

    @Test
    void findBrandByIdShouldReturnNullWhenFound(){
        when(brandRepository.findById(1L)).thenReturn(Optional.of(brandEntity));
        when(mapper.toBrandOptional(Optional.of(brandEntity))).thenReturn(Optional.of(brand));

        Optional<Brand> result = brandAdapter.findBrandById(1L);

        assertTrue(result.isPresent());
        assertEquals(brand, result.get());
    }

    @Test
    void getAllBrandsShouldReturnPaginatedBrandsWhenOrderIsAsc(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BrandEntity> page = new PageImpl<>(List.of(brandEntity, brandEntityTwo), pageRequest, 2);
        when(brandRepository.findAllByOrderByNameAsc(pageRequest)).thenReturn(page);
        when(mapper.toBrandList(page.getContent())).thenReturn(List.of(brand, brandTwo));

        PaginationInfo<Brand> result = brandAdapter.getAllBrands(0, 10,"ASC");
        System.out.println(result.getList().get(0).getName());
        System.out.println(result.getList().get(1).getName());
        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(brand, result.getList().get(0));
    }

    @Test
    void getAllBrandsShouldReturnPaginatedBrandsWhenOrderIsDesc(){
        PageRequest pageRequest = PageRequest.of(0, 10);
        Page<BrandEntity> page = new PageImpl<>(List.of(brandEntityTwo, brandEntity), pageRequest, 2);
        when(brandRepository.findAllByOrderByNameDesc(pageRequest)).thenReturn(page);
        when(mapper.toBrandList(page.getContent())).thenReturn(List.of(brandTwo, brand));

        PaginationInfo<Brand> result = brandAdapter.getAllBrands(0, 10,"DESC");

        assertNotNull(result);
        assertEquals(2, result.getTotalElements());
        assertEquals(brandTwo, result.getList().get(0));
    }
}