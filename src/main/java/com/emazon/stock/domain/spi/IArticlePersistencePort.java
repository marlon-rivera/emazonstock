package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;

import java.util.List;

public interface IArticlePersistencePort {

    void saveArticle(Article article);
    PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories);


}
