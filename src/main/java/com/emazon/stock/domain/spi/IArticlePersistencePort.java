package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

public interface IArticlePersistencePort {

    void saveArticle(Article article);
    Optional<Article> getArticleById(Long id);
    PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories);
    void increaseStockArticle(Article article);
    BigInteger getQuantityArticle(Long id);
    PaginationInfo<Article> findByIdsAndCategoriesAndBrands(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories, List<Long> idsBrands);
    PaginationInfo<Article> findByCategoriesIdsAndIds(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories);
    PaginationInfo<Article> findByBrandIdsAndIds(int page, int size, List<Long> idsArticles, String order, List<Long> idsBrands);
    PaginationInfo<Article> findByIds(int page, int size, List<Long> idsArticles, String order);


}
