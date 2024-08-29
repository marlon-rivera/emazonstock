package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.ArticleRequest;
import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.dto.request.CategoryRequest;
import com.emazon.stock.adapters.driving.http.mapper.request.IArticleRequestMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.domain.api.IArticleServicePort;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Category;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleServicePort articleServicePort;
    private final IArticleRequestMapper articleRequestMapper;

    @Operation(summary = "Create a new article", description = "This endpoint allows you to create a new article.")
    @ApiResponse(responseCode = "201", description = "Article created correctly.", content = @Content)
    @ApiResponse(responseCode = "400", description = "Incorrect article creation request", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @PostMapping("/")
    public ResponseEntity<Void> saveArticle(@Valid @RequestBody ArticleRequest articleRequest) {
        Set<Category> categoryRequestList = articleRequest.getCategoriesIds().stream().map(id -> new Category(id, null, null)).collect(Collectors.toSet());
        Article article = articleRequestMapper.toArticle(articleRequest);
        article.setCategories(categoryRequestList);
        articleServicePort.saveArticle(article);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

}
