package com.emazon.stock.domain.api;

import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;

public interface IBrandServicePort {

    void saveBrand(Brand brand);
    PaginationInfo<Brand> getAllBrands(int page, int size, String order);

}
