package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.IArticleServicePort;
import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.exception.article.ArticleExceedsCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleMinimumCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
import com.emazon.stock.domain.exception.article.ArticleWithRepeatedCategoriesException;
import com.emazon.stock.domain.exception.brand.BrandNoDataFoundException;
import com.emazon.stock.domain.exception.category.CategoryNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;


import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@RequiredArgsConstructor
public class ArticleUseCaseImpl implements IArticleServicePort {

    private final IArticlePersistencePort iArticlePersistencePort;
    private final IBrandServicePort iBrandServicePort;
    private final ICategoryServicePort categoryServicePort;

    @Override
    public void saveArticle(Article article) {
        if (article.getCategories() == null || article.getCategories().isEmpty()) {
            throw new ArticleMinimumCategoriesException();
        }
        if(article.getCategories().size() > Constants.MAX_CATEGORIES_BY_ARTICLE){
            throw new ArticleExceedsCategoriesException();
        }
        Set<Long> categoriesIds = new HashSet<>();
        verifyExistsCategories(categoriesIds, article);
        if(categoriesIds.size() != article.getCategories().size()) {
            throw new ArticleWithRepeatedCategoriesException();
        }
        if (iBrandServicePort.getBrandById(article.getBrand().getId()).isEmpty()){
            throw new BrandNoDataFoundException();
        }
        iArticlePersistencePort.saveArticle(article);
    }

    private void verifyExistsCategories(Set<Long> categoriesIds, Article article) {
        for (Category category : article.getCategories()) {
            if(categoryServicePort.getCategoryById(category.getId()).isEmpty()) {
                throw new CategoryNoDataFoundException();
            }
            categoriesIds.add(category.getId());
            article.addCategory(category);
        }
    }


    @Override
    public PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories) {
        PaginationInfo<Article> articles = iArticlePersistencePort.getAllArticles(page, size, sortBy, sortDirection, idsCategories);
        if(articles.getList().isEmpty()){
            throw new ArticleNoDataFoundException();
        }
        return articles;
    }

    @Override
    public void increaseStockArticle(Long id, BigInteger quantity) {
        Optional<Article> articleOptional = iArticlePersistencePort.getArticleById(id);
        if(articleOptional.isEmpty()) {
            throw new ArticleNoDataFoundException();
        }
        Article article = articleOptional.get();
        article.setQuantity(article.getQuantity() + quantity.intValue());
        iArticlePersistencePort.increaseStockArticle(article);
    }

    @Override
    public BigInteger getQuantityArticle(Long id) {
        return iArticlePersistencePort.getQuantityArticle(id);
    }

    @Override
    public List<Long> getCategoriesIds(Long idArticle) {
        Optional<Article> articleOptional = iArticlePersistencePort.getArticleById(idArticle);
        if(articleOptional.isEmpty()){
            throw new ArticleNoDataFoundException();
        }
        return articleOptional.get().getCategories().stream().map(Category::getId).toList();
    }
}
