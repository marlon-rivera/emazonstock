package com.emazon.stock.adapters.driven.jpa.mysql.mapper;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.domain.model.Article;
import org.mapstruct.Mapper;

import java.util.List;
import java.util.Optional;

@Mapper(componentModel = "spring")
public interface IArticleEntityMapper {

    ArticleEntity toArticleEntity(Article article);

    Article toArticle(ArticleEntity articleEntity);

    List<Article> toArticleList(List<ArticleEntity> articleEntities);

    default Optional<Article> toArticleOptional(Optional<ArticleEntity> articleEntity){
        return articleEntity.map(this::toArticle);
    }

}
