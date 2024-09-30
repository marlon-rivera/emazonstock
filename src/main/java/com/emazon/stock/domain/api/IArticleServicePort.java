package com.emazon.stock.domain.api;

import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Map;

public interface IArticleServicePort {

    void saveArticle(Article article);
    PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories);
    void increaseStockArticle(Long id, BigInteger quantity);
    BigInteger getQuantityArticle(Long id);
    List<Long> getCategoriesIds(Long idArticle);
    PaginationInfo<Article> getArticlesOfShoppingCart(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories, List<Long> idsBrands);
    Article decreaseStockArticle(Long id, BigInteger quantity, Map<Long, BigInteger> originalQuantities);
    void purchase(List<Long> idsArticles, List<BigInteger> quantities);



}
