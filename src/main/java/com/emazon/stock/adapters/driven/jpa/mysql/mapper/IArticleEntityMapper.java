package com.emazon.stock.adapters.driven.jpa.mysql.mapper;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.domain.model.Article;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface IArticleEntityMapper {

    ArticleEntity toArticleEntity(Article article);

    Article toArticle(ArticleEntity articleEntity);

}
