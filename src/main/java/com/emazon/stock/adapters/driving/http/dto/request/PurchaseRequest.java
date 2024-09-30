package com.emazon.stock.adapters.driving.http.dto.request;

import com.emazon.stock.utils.Constants;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class PurchaseRequest {

    @NotNull(message = Constants.EXCEPTION_IDS_ARTICLES_NOT_BE_EMPTY)
    @NotEmpty(message = Constants.EXCEPTION_IDS_ARTICLES_NOT_BE_EMPTY)
    private List<Long> idsArticles;
    @NotNull(message = Constants.EXCEPTION_QUANTITIES_ARTICLES_NOT_BE_EMPTY)
    @NotEmpty(message = Constants.EXCEPTION_QUANTITIES_ARTICLES_NOT_BE_EMPTY)
    private List<BigInteger> quantities;

}
