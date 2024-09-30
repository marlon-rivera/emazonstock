package com.emazon.stock.domain.exception.article;

public class ArticleInsufficientStockToPurchaseException extends RuntimeException {
    public ArticleInsufficientStockToPurchaseException(String message) {
        super(message);
    }
}
