package com.emazon.stock.adapters.driving.http.mapper.response;

import com.emazon.stock.adapters.driving.http.dto.response.ArticleResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.List;

@Mapper(componentModel = "spring")
public interface IArticleResponseMapper {

    @Mapping(target = "id", source = "article.id")
    @Mapping(target = "name", source = "article.name")
    @Mapping(target = "description", source = "article.description")
    @Mapping(target = "quantity", source = "article.quantity")
    @Mapping(target = "price", source = "article.price")
    @Mapping(target = "brand", source = "article.brand")
    @Mapping(target = "categories", source = "article.categories")
    ArticleResponse toArticleResponse(Article article);

    List<ArticleResponse> toListArticleResponse(List<Article> articles);

    PaginationInfoResponse<ArticleResponse> toPaginationInfoResponse(PaginationInfo<Article> paginationInfo);
}
