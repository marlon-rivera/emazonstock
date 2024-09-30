package com.emazon.stock.adapters.driving.http.dto.request;

import com.emazon.stock.domain.model.Article;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.math.BigInteger;
import java.util.List;

@Getter
@Setter
@AllArgsConstructor
public class SaleRequest {

    private List<Article> articles;
    private List<BigInteger> quantities;

}
