package com.emazon.stock.configuration.exceptionhandler;

import com.emazon.stock.domain.exception.article.ArticleExceedsCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleMinimumCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
import com.emazon.stock.domain.exception.article.ArticleWithRepeatedCategoriesException;
import com.emazon.stock.utils.Constants;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;

@ControllerAdvice
public class ArticleControllerAdvisor {

    @ExceptionHandler(ArticleWithRepeatedCategoriesException.class)
    public ResponseEntity<ExceptionResponse> handleArticleWithRepeatedCategories(ArticleWithRepeatedCategoriesException ex){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_ARTICLE_WITH_REPEATED_CATEGORIES, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(ArticleExceedsCategoriesException.class)
    public ResponseEntity<ExceptionResponse> handleArticleExceedsCategories(ArticleExceedsCategoriesException ex){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_ARTICLE_EXCEEDS_CATEGORIES, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(ArticleMinimumCategoriesException.class)
    public ResponseEntity<ExceptionResponse> handleArticleMinimumCategories(ArticleMinimumCategoriesException ex){
        return ResponseEntity.badRequest().body(
                new ExceptionResponse(Constants.EXCEPTION_ARTICLE_MINIMUM_CATEGORIES, HttpStatus.BAD_REQUEST.toString(), LocalDateTime.now())
        );
    }

    @ExceptionHandler(ArticleNoDataFoundException.class)
    public ResponseEntity<ExceptionResponse> handleArticleNoDataFound(ArticleNoDataFoundException ex){
        return new ResponseEntity<>(new ExceptionResponse(Constants.EXCEPTION_ARTICLE_NO_DATA_FOUND, HttpStatus.NOT_FOUND.toString(), LocalDateTime.now()), HttpStatus.NOT_FOUND);
    }

}
