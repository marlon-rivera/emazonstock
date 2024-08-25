package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.exception.brand.*;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.spi.IBrandPersistencePort;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BrandUseCaseImpl implements IBrandServicePort {

    private final IBrandPersistencePort brandPersistencePort;

    @Override
    public void saveBrand(Brand brand) {
        brand.setName(brand.getName().trim());
        brand.setDescription(brand.getDescription().trim());
        if(brandPersistencePort.findCategoryByName(brand.getName()).isPresent()){
            throw new BrandAlreadyExistsException();
        }
        if(brand.getName().isBlank()){
            throw new BrandNameBlankException();
        }
        if(brand.getDescription().isBlank()){
            throw new BrandDescriptionBlankException();
        }
        if(brand.getName().length() > Constants.MAX_CHARACTERS_NAME_BRAND){
            throw new BrandMaximumNumberCharactersNameException();
        }
        if(brand.getDescription().length() > Constants.MAX_CHARACTERS_DESCRIPTION_BRAND){
            throw new BrandMaximumNumberCharactersDescriptionException();
        }
        brandPersistencePort.saveBrand(brand);
    }
}
