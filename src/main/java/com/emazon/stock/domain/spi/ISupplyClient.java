package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Article;

import java.math.BigInteger;
import java.util.List;

public interface ISupplyClient {

    void sendSale(List<Article> articles, List<BigInteger> quantities);

}
