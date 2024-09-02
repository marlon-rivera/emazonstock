package com.emazon.stock.domain.api;

import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;

import java.util.List;

public interface IArticleServicePort {

    void saveArticle(Article article);
    PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories);


}
