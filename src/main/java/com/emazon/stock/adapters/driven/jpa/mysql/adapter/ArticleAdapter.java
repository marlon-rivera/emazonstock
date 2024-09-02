package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IArticleEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IArticleRepository;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.ICategoryRepository;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import com.emazon.stock.utils.Constants;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@RequiredArgsConstructor
public class ArticleAdapter implements IArticlePersistencePort {

    private final IArticleRepository articleRepository;
    private final IArticleEntityMapper articleEntityMapper;

    @Override
    public void saveArticle(Article article) {
        articleRepository.save(articleEntityMapper.toArticleEntity(article));
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
