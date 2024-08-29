package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.exception.brand.*;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IBrandPersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class BrandUseCaseImplTest {

    @Mock
    private IBrandPersistencePort brandPersistencePort;

    @InjectMocks
    private BrandUseCaseImpl brandUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveBrandSuccesfully() {
        String name = "NewBrand";
        Brand brand = new Brand(1L, name, "Description for newbrand");

        when(brandPersistencePort.findBrandByName(name)).thenReturn(Optional.empty());

        brandUseCase.saveBrand(brand);

        verify(brandPersistencePort, times(1)).saveBrand(any(Brand.class));
    }

    @Test
    void testSaveBrandWithLongNameShouldFail(){
        String longName = "ThisBrandNameIsWayTooLongAndShouldFailCauseAValidationError";
        Brand brand = new Brand(1L, longName, "Description for newbrand");

        assertThrows(BrandMaximumNumberCharactersNameException.class, () -> brandUseCase.saveBrand(brand));
    }

    @Test
    void testSaveBrandWithLongDescriptionShouldFail(){
        String longDescription = "This description is way too long an should cause a validation error because it exceeds the maximun length allowed by the application rules.";
        Brand brand = new Brand(1L, "Brand", longDescription);
        assertThrows(BrandMaximumNumberCharactersDescriptionException.class, () -> brandUseCase.saveBrand(brand));
    }

    @Test
    void testSaveBrandWithEmptyNameShouldFail(){
        Brand brand = new Brand(1L, "", "description");
        assertThrows(BrandNameBlankException.class, () -> brandUseCase.saveBrand(brand));
    }

    @Test
    void testSaveBrandWithEmptyDescriptionShouldFail(){
        Brand brand = new Brand(1L, "Brand", "");
        assertThrows(BrandDescriptionBlankException.class, () -> brandUseCase.saveBrand(brand));
    }

    @Test
    void testSaveBrandThatAlreadyExistsShouldFail(){
        Brand brand = new Brand(1L, "Brand", "Description for newbrand");
        when(brandPersistencePort.findBrandByName("Brand")).thenReturn(Optional.of(brand));
        assertThrows(BrandAlreadyExistsException.class, () -> brandUseCase.saveBrand(brand));
    }

    @Test
    void  testGetAllBrandsSuccess(){
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(new Brand(1L, "Brand", "Description for newbrand")),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandPersistencePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        PaginationInfo<Brand> result = brandUseCase.getAllBrands(1, 10, "ASC");

        assertNotNull(result);
        assertFalse(result.getList().isEmpty());
        assertEquals("Brand", result.getList().get(0).getName());
    }

    @Test
    void testGetAllCategoriesNoData(){
        PaginationInfo<Brand> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(brandPersistencePort.getAllBrands(anyInt(), anyInt(), anyString())).thenReturn(paginationInfo);

        assertThrows(BrandNoDataFoundException.class,() ->
                brandUseCase.getAllBrands(0, 10, "ASC"));
    }

}