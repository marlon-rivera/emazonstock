package com.emazon.stock.adapters.driving.http.mapper.request;

import com.emazon.stock.adapters.driving.http.dto.request.ArticleRequest;
import com.emazon.stock.domain.model.Article;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring")
public interface IArticleRequestMapper {

    @Mapping(target = "id", ignore = true)
    @Mapping(target = "name", source = "articleRequest.name")
    @Mapping(target = "description", source = "articleRequest.description")
    @Mapping(target = "quantity", source = "articleRequest.quantity")
    @Mapping(target = "price", source = "articleRequest.price")
    @Mapping(target = "categories", ignore = true)
    @Mapping(target = "brand", source = "articleRequest.brand")
    Article toArticle(ArticleRequest articleRequest);



}
