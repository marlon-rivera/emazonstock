package com.emazon.stock.adapters.driven.feign;

import com.emazon.stock.adapters.driving.http.dto.request.SaleRequest;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.spi.ISupplyClient;
import lombok.RequiredArgsConstructor;

import java.math.BigInteger;
import java.util.List;

@RequiredArgsConstructor
public class SupplyFeignAdapter implements ISupplyClient {

    private final ISupplyFeignClient supplyFeignClient;

    @Override
    public void sendSale(List<Article> articles, List<BigInteger> quantities) {
        supplyFeignClient.addSale(new SaleRequest(articles, quantities));
    }

}
