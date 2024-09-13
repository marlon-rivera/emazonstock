package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.ArticleRequest;
import com.emazon.stock.adapters.driving.http.dto.response.ArticleResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.IArticleRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.IArticleResponseMapper;
import com.emazon.stock.configuration.exceptionhandler.ExceptionResponse;
import com.emazon.stock.configuration.exceptionhandler.ValidationExceptionResponse;
import com.emazon.stock.domain.api.IArticleServicePort;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.utils.Constants;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.Pattern;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigInteger;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/article")
@RequiredArgsConstructor
public class ArticleController {

    private final IArticleServicePort articleServicePort;
    private final IArticleRequestMapper articleRequestMapper;
    private final IArticleResponseMapper articleResponseMapper;

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

    @Operation(summary = "List all articles with pagination and sorting", description = "This endpoint allows you to list all availables articles. Query parameters can be used to control the pagination and order of the results.")
    @ApiResponse(responseCode = "200", description = "Correctly listed articles", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = PaginationInfoResponse.class))})
    @ApiResponse(responseCode = "404", description = "No articles found for listing", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ExceptionResponse.class))})
    @ApiResponse(responseCode = "400", description = "Invalid request due to incorrect parameters. Verify that the parameters sent comply with the expected format and are valid.", content = {@Content(mediaType = "application/json", schema = @Schema(implementation = ValidationExceptionResponse.class))})
    @GetMapping("/all")
    public ResponseEntity<PaginationInfoResponse<ArticleResponse>> getAllArticles(
            @Parameter(description = "Page number to list", example = "0")
            @RequestParam(value = "page", defaultValue = Constants.DEFAULT_VALUE_NUMBER_PAGE) @Min(value = Constants.MIN_VALUE_PAGE, message = Constants.EXCEPTION_MIN_VALUE_PAGE) int page,
            @Parameter(description = "Number of categories by page", example = "10")
            @RequestParam(value = "size", defaultValue = Constants.DEFAULT_VALUE_SIZE_PAGE) @Min(value = Constants.MIN_VALUES_PER_PAGE, message = Constants.EXCEPTION_MIN_VALUES_PER_PAGE) int size,
            @Parameter(description = "Sort order (e.g. 'ASC' for ascending or 'DESC' for descending)", example = "ASC")
            @RequestParam(value = "order", defaultValue = Constants.ORDER_ASC) @Pattern(regexp = Constants.REGEX_ORDER, message = Constants.EXCEPTION_REGEX_ORDER) String order,
            @Parameter(description = "ordering criteria", example = "brand | name")
            @RequestParam(value = "sortBy", defaultValue = Constants.ARTICLE_FIND_BY_NAME) @Pattern(regexp = Constants.REGEX_ORDERING_CRITERIA_ARTICLE, message = Constants.EXCEPTION_REGEX_ORDER) String sortBy,
            @Parameter(description = "List of ids contained in the articles", example = "1, 2, 3")
            @RequestParam(value = "idsCategories", defaultValue = "1, 2, 3") List<Long> idsCategories
    ){

        return ResponseEntity.ok(
                articleResponseMapper.toPaginationInfoResponse(articleServicePort.getAllArticles(page, size, sortBy, order, idsCategories))
        );

    }

    @Operation(summary = "Add supply to article", description = "This endpoint allows you to add supply to an article")
    @ApiResponse(responseCode = "200", description = "Supply added correctly.", content = @Content)
    @PutMapping("/increase/{id}")
    public ResponseEntity<Void> increaseArticleStock(@PathVariable Long id, @RequestBody BigInteger quantity){
        articleServicePort.increaseStockArticle(id, quantity);
        return ResponseEntity.ok().build();
    }

}
