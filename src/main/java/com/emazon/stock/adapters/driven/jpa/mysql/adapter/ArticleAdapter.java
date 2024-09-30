package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IArticleEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IArticleRepository;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import com.emazon.stock.utils.Constants;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.math.BigInteger;
import java.util.*;

@RequiredArgsConstructor
public class ArticleAdapter implements IArticlePersistencePort {

    private final IArticleRepository articleRepository;
    private final IArticleEntityMapper articleEntityMapper;

    @Override
    public void saveArticle(Article article) {
        articleRepository.saveAndFlush(articleEntityMapper.toArticleEntity(article));
    }

    @Override
    public Optional<Article> getArticleById(Long id) {
        return articleEntityMapper.toArticleOptional(articleRepository.findById(id));
    }


    @Override
    public PaginationInfo<Article> getAllArticles(int page, int size, String sortBy, String sortDirection, List<Long> idsCategories) {
        Pageable pageable;
        Sort.Direction direction = sortDirection.equalsIgnoreCase(Constants.ORDER_ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        if(sortBy.equalsIgnoreCase(Constants.ARTICLE_FIND_BY_NAME)){
            pageable = PageRequest.of(page, size, Sort.by(direction, Constants.ARTICLE_FIND_BY_NAME));
        }else{
            pageable = PageRequest.of(page, size, Sort.by(direction, Constants.ARTICLE_FIND_BY_BRAND_NAME));
        }
        Page<ArticleEntity> articleEntities;
        if(!idsCategories.isEmpty()){
            articleEntities = articleRepository.findByCategoriaIds(idsCategories, pageable);
        }else{
            articleEntities = articleRepository.findAll(pageable);
        }

        return convertPageToPaginationInfo(articleEntities);
    }

    @Override
    public void increaseStockArticle(Article article) {
        articleRepository.save(articleEntityMapper.toArticleEntity(article));
    }

    @Override
    public BigInteger getQuantityArticle(Long id) {
        Optional<ArticleEntity> articleOptional = articleRepository.findById(id);
        if(articleOptional.isEmpty()){
            throw new ArticleNoDataFoundException();
        }
        ArticleEntity articleEntity = articleOptional.get();
        return new BigInteger(Integer.toString(articleEntity.getQuantity()));
    }

    @Override
    public PaginationInfo<Article> findByIdsAndCategoriesAndBrands(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories, List<Long> idsBrands) {
        Pageable pageable = createPageable(page, size, order);
        Page<ArticleEntity> articleEntities = articleRepository.findByIdsAndCategoriesAndBrands(idsArticles, idsCategories, idsBrands, pageable);
        return convertPageToPaginationInfo(articleEntities);
    }

    @Override
    public PaginationInfo<Article> findByCategoriesIdsAndIds(int page, int size, List<Long> idsArticles, String order, List<Long> idsCategories) {
        Pageable pageable = createPageable(page, size, order);
        Page<ArticleEntity> articleEntities = articleRepository.findByCategoriesIdsAndIds(idsCategories, idsArticles, pageable);
        return convertPageToPaginationInfo(articleEntities);
    }

    @Override
    public PaginationInfo<Article> findByBrandIdsAndIds(int page, int size, List<Long> idsArticles, String order, List<Long> idsBrands) {
        Pageable pageable = createPageable(page, size, order);
        Page<ArticleEntity> articleEntities = articleRepository.findByBrandIdsAndIds(idsBrands, idsArticles, pageable);
        return convertPageToPaginationInfo(articleEntities);
    }

    @Override
    public PaginationInfo<Article> findByIds(int page, int size, List<Long> idsArticles, String order) {
        Pageable pageable = createPageable(page, size, order);
        Page<ArticleEntity> articleEntities = articleRepository.findByIds(idsArticles, pageable);
        return convertPageToPaginationInfo(articleEntities);
    }

    private Pageable createPageable(int page, int size, String order){
        Sort.Direction direction = order.equalsIgnoreCase(Constants.ORDER_ASC) ? Sort.Direction.ASC : Sort.Direction.DESC;
        return PageRequest.of(page, size, Sort.by(direction, Constants.ARTICLE_FIND_BY_NAME));
    }

    private PaginationInfo<Article> convertPageToPaginationInfo(Page<ArticleEntity> articleEntities) {
        return new PaginationInfo<>(
                articleEntityMapper.toArticleList(articleEntities.getContent()),
                articleEntities.getNumber(),
                articleEntities.getSize(),
                articleEntities.getTotalElements(),
                articleEntities.getTotalPages(),
                articleEntities.hasNext(),
                articleEntities.hasPrevious()
        );
    }
}
