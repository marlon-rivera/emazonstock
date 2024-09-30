package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.IArticleServicePort;
import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.exception.article.*;
import com.emazon.stock.domain.exception.brand.BrandNoDataFoundException;
import com.emazon.stock.domain.exception.category.CategoryNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.model.Verification;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import com.emazon.stock.domain.spi.IReportClient;
import com.emazon.stock.domain.spi.ISupplyClient;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;


import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
public class ArticleUseCaseImpl implements IArticleServicePort {

    private final IArticlePersistencePort iArticlePersistencePort;
    private final IBrandServicePort iBrandServicePort;
    private final ICategoryServicePort categoryServicePort;
    private final IReportClient reportClient;
    private final ISupplyClient supplyClient;

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

    @Override
    public PaginationInfo<Article> getArticlesOfShoppingCart(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories, List<Long> idsBrands) {
        if(!idsCategories.isEmpty() && !idsBrands.isEmpty()) {
            return iArticlePersistencePort.findByIdsAndCategoriesAndBrands(page, size, idsArticles, order, idsCategories, idsBrands);
        }
        if(!idsBrands.isEmpty()) {
            return iArticlePersistencePort.findByBrandIdsAndIds(page, size, idsArticles, order, idsBrands);
        }
        if(!idsCategories.isEmpty()){
            return iArticlePersistencePort.findByCategoriesIdsAndIds(page, size, idsArticles, order, idsCategories);
        }
        return iArticlePersistencePort.findByIds(page, size, idsArticles, order);
    }

    @Override
    public void purchase(List<Long> idsArticles, List<BigInteger> quantities) {
        Map<Long, BigInteger> originalQuantities = new HashMap<>();
        List<Article> articlesToSave = new ArrayList<>();

        for (int i = 0; i < idsArticles.size(); i++) {
            Long id = idsArticles.get(i);
            BigInteger quantity = quantities.get(i);
            validateStockArticle(id, quantity, originalQuantities);
        }
        for (int i = 0; i < idsArticles.size(); i++) {
            Article article = decreaseStockArticle(idsArticles.get(i), quantities.get(i), originalQuantities);
            articlesToSave.add(article);
        }
        for (Article article : articlesToSave) {
            iArticlePersistencePort.saveArticle(article);
        }

        sendSale(articlesToSave, quantities);

    }

    private void validateStockArticle(Long id, BigInteger quantity, Map<Long, BigInteger> originalQuantities) {
        Optional<Article> articleOp = iArticlePersistencePort.getArticleById(id);
        if (articleOp.isEmpty()) {
            throw new ArticleNoDataFoundException();
        }
        Article article = articleOp.get();
        originalQuantities.put(article.getId(), BigInteger.valueOf(article.getQuantity()));

        if (article.getQuantity() - quantity.intValue() < 0) {
            reportClient.addVerification(new Verification(article.getId(), article.getQuantity(), quantity.intValue(), Constants.STATUS_FAIL, article.getPrice()));
            throw new ArticleInsufficientStockToPurchaseException(article.getName());
        }
    }

    @Override
    public Article decreaseStockArticle(Long id, BigInteger quantity, Map<Long, BigInteger> originalQuantities) {
        Optional<Article> articleOp = iArticlePersistencePort.getArticleById(id);
        if (articleOp.isEmpty()) {
            throw new ArticleNoDataFoundException();
        }
        Article article = articleOp.get();

        article.setQuantity(article.getQuantity() - quantity.intValue());
        reportClient.addVerification(new Verification(article.getId(), article.getQuantity(), quantity.intValue(), Constants.STATUS_SUCCESS, article.getPrice()));
        return article;
    }

    private void sendSale(List<Article> articles, List<BigInteger> quantities){
        supplyClient.sendSale(articles, quantities);
    }
}
