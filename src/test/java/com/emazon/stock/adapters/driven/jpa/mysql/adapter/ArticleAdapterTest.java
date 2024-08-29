package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IArticleEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IArticleRepository;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.Category;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.Set;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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





}