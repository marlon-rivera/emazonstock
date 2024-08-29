package com.emazon.stock.adapters.driving.http.dto.request;

import com.emazon.stock.utils.Constants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.UniqueElements;

import java.math.BigDecimal;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class ArticleRequest {

    @NotEmpty(message = Constants.ARTICLE_NAME_MUST_MANDATORY)
    private String name;

    @NotEmpty(message = Constants.ARTICLE_DESCRIPTION_MUST_MANDATORY)
    private String description;

    @NotNull(message = Constants.ARTICLE_QUANTITY_MUST_MANDATORY)
    private Integer quantity;

    @NotNull(message = Constants.ARTICLE_PRICE_MUST_MANDATORY)
    private BigDecimal price;

    @NotNull(message = Constants.EXCEPTION_ARTICLE_MINIMUM_CATEGORIES)
    @Size(min = Constants.MIN_CATEGORIES_BY_ARTICLE, message = Constants.EXCEPTION_ARTICLE_MINIMUM_CATEGORIES)
    @Size(max = Constants.MAX_CATEGORIES_BY_ARTICLE, message = Constants.EXCEPTION_ARTICLE_EXCEEDS_CATEGORIES)
    @UniqueElements(message = Constants.EXCEPTION_ARTICLE_WITH_REPEATED_CATEGORIES)
    private List<Long> categoriesIds;

    @NotNull(message = Constants.ARTICLE_BRAND_MUST_MANDATORY)
    private BrandRequest brand;

}
