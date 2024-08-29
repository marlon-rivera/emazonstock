package com.emazon.stock.adapters.driving.http.dto.request;

import com.emazon.stock.utils.Constants;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class BrandRequest {

    private Long id;
    @NotNull(message = Constants.EXCEPTION_BRAND_NAME_BLANK)
    @Size(min = Constants.MIN_CHARACTERS, message = Constants.EXCEPTION_BRAND_NAME_BLANK)
    @Size(max = Constants.MAX_CHARACTERS_NAME_BRAND, message = Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_NAME)
    private String name;
    @NotNull(message = Constants.EXCEPTION_BRAND_DESCRIPTION_BLANK)
    @Size(min = Constants.MIN_CHARACTERS, message = Constants.EXCEPTION_BRAND_DESCRIPTION_BLANK)
    @Size(max = Constants.MAX_CHARACTERS_DESCRIPTION_BRAND, message = Constants.EXCEPTION_BRAND_MAXIMUM_NUMBER_CHARACTERS_DESCRIPTION)
    private String description;

}
