package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IArticleEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IArticleRepository;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.utils.Constants;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.*;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.mockito.ArgumentMatchers.anyList;
import static org.mockito.Mockito.*;

class ArticleAdapterTest {
    @Mock
   private IArticleRepository iArticleRepository;

   @Mock
   private IArticleEntityMapper articleEntityMapper;

   @InjectMocks
    private ArticleAdapter articleAdapter;

   private Article article;
   private ArticleEntity articleEntity;

   @BeforeEach
    void setUp() {
       MockitoAnnotations.openMocks(this);
       article = new Article(1L, "name", "description", 1, BigDecimal.ONE, new Brand(1L, "name", "description"));
       article.addCategory(new Category(1L, "name", "description"));

       articleEntity = new ArticleEntity(1L, "name", "description", 1, BigDecimal.ONE, new BrandEntity(1L, "name", "description"), Set.of(new CategoryEntity(1L, "name", "description")));
   }

   @Test
    void saveArticleShouldSaveArticle() {
       when(articleEntityMapper.toArticleEntity(article)).thenReturn(articleEntity);

       articleAdapter.saveArticle(article);

       verify(iArticleRepository).save(articleEntity);
   }

   @Test
   void testGetAllArticles_WithCategories() {
      List<Long> categoryIds = List.of(1L, 2L);
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Constants.ARTICLE_FIND_BY_NAME));
      Page<ArticleEntity> articlePage = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findByCategoriaIds(categoryIds, pageable)).thenReturn(articlePage);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.getAllArticles(0, 10, Constants.ARTICLE_FIND_BY_NAME, "asc", categoryIds);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository, times(1)).findByCategoriaIds(categoryIds, pageable);
   }

   @Test
   void testGetAllArticles_WithoutCategories() {
      List<Long> categoryIds = List.of();
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.DESC, Constants.ARTICLE_FIND_BY_BRAND_NAME));
      Page<ArticleEntity> articlePage = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findAll(pageable)).thenReturn(articlePage);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.getAllArticles(0, 10, Constants.ARTICLE_FIND_BY_BRAND_NAME, "desc", categoryIds);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository, times(1)).findAll(pageable);
   }

   @Test
   void increaseStockArticleShouldSaveArticle() {
      when(articleEntityMapper.toArticleEntity(article)).thenReturn(articleEntity);

      articleAdapter.increaseStockArticle(article);

      verify(iArticleRepository).save(articleEntity);
   }

}