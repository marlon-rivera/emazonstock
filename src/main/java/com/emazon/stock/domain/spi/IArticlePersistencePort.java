package com.emazon.stock.domain.spi;

import com.emazon.stock.domain.model.Article;

public interface IArticlePersistencePort {

    void saveArticle(Article article);

}
