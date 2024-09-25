package com.emazon.stock.adapters.driven.jpa.mysql.adapter;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.BrandEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.entity.CategoryEntity;
import com.emazon.stock.adapters.driven.jpa.mysql.mapper.IArticleEntityMapper;
import com.emazon.stock.adapters.driven.jpa.mysql.repository.IArticleRepository;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
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
import java.math.BigInteger;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;
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

   @Test
   void getQuantityArticle_ShouldReturnQuantity_WhenArticleExists() {
      Long idArticle = 1L;
      int quantity = 100;
      ArticleEntity articleEntity = new ArticleEntity();
      articleEntity.setQuantity(quantity);

      when(iArticleRepository.findById(idArticle)).thenReturn(Optional.of(articleEntity));

      BigInteger actualQuantity = articleAdapter.getQuantityArticle(idArticle);

      assertEquals(BigInteger.valueOf(quantity), actualQuantity);
   }

   @Test
   void getQuantityArticle_ShouldThrowArticleNoDataFoundException_WhenArticleDoesNotExist() {
      Long idArticle = 999L;

      when(iArticleRepository.findById(idArticle)).thenReturn(Optional.empty());

      assertThrows(ArticleNoDataFoundException.class, () -> articleAdapter.getQuantityArticle(idArticle));
   }

   @Test
   void findByIdsAndCategoriesAndBrands_shouldReturnPaginationInfo() {
      List<Long> idsArticles = List.of(1L, 2L, 3L);
      List<Long> idsCategories = List.of(1L, 2L, 3L);
      List<Long> idsBrands = List.of(1L);
      String order = "ASC";
      int page = 0;
      int size = 10;

      Pageable pageable = PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, Constants.ARTICLE_FIND_BY_NAME));
      Page<ArticleEntity> articleEntities = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findByIdsAndCategoriesAndBrands(idsArticles, idsCategories, idsBrands, pageable))
              .thenReturn(articleEntities);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.findByIdsAndCategoriesAndBrands(page, size, idsArticles, order, idsCategories, idsBrands);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository).findByIdsAndCategoriesAndBrands(idsArticles, idsCategories, idsBrands, pageable);
   }


   @Test
   void testFindByCategoriesIdsAndIds() {
      List<Long> idsArticles = List.of(1L);
      List<Long> idsCategories = List.of(1L);
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Constants.ARTICLE_FIND_BY_NAME));

      Page<ArticleEntity> articlePage = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findByCategoriesIdsAndIds(idsCategories, idsArticles, pageable)).thenReturn(articlePage);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.findByCategoriesIdsAndIds(0, 10, idsArticles, "ASC", idsCategories);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository).findByCategoriesIdsAndIds(idsCategories, idsArticles, pageable);
   }

   @Test
   void testFindByBrandIdsAndIds() {
      List<Long> idsArticles = List.of(1L);
      List<Long> idsBrands = List.of(1L);
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Constants.ARTICLE_FIND_BY_NAME));

      Page<ArticleEntity> articlePage = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findByBrandIdsAndIds(idsBrands, idsArticles, pageable)).thenReturn(articlePage);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.findByBrandIdsAndIds(0, 10, idsArticles, "ASC", idsBrands);

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository).findByBrandIdsAndIds(idsBrands, idsArticles, pageable);
   }

   @Test
   void testFindByIds() {
      List<Long> idsArticles = List.of(1L);
      Pageable pageable = PageRequest.of(0, 10, Sort.by(Sort.Direction.ASC, Constants.ARTICLE_FIND_BY_NAME));

      Page<ArticleEntity> articlePage = new PageImpl<>(List.of(articleEntity), pageable, 1);

      when(iArticleRepository.findByIds(idsArticles, pageable)).thenReturn(articlePage);
      when(articleEntityMapper.toArticleList(anyList())).thenReturn(List.of(article));

      PaginationInfo<Article> result = articleAdapter.findByIds(0, 10, idsArticles, "ASC");

      assertNotNull(result);
      assertEquals(1, result.getTotalElements());
      assertEquals(article, result.getList().get(0));
      verify(iArticleRepository).findByIds(idsArticles, pageable);
   }

   @Test
   void getArticleById_ShouldReturnArticle_WhenArticleExists() {
      Long idArticle = 1L;
      Optional<ArticleEntity> articleEntityOptional = Optional.of(articleEntity); // articleEntity definido en el setup
      Optional<Article> articleOptional = Optional.of(article); // article definido en el setup

      when(iArticleRepository.findById(idArticle)).thenReturn(articleEntityOptional);
      when(articleEntityMapper.toArticleOptional(articleEntityOptional)).thenReturn(articleOptional);

      Optional<Article> result = articleAdapter.getArticleById(idArticle);

      assertTrue(result.isPresent());
      assertEquals(article, result.get());

      verify(iArticleRepository).findById(idArticle);
      verify(articleEntityMapper).toArticleOptional(articleEntityOptional);
   }

   @Test
   void getArticleById_ShouldReturnEmptyOptional_WhenArticleDoesNotExist() {
      Long idArticle = 999L;
      Optional<ArticleEntity> emptyArticleEntityOptional = Optional.empty();

      when(iArticleRepository.findById(idArticle)).thenReturn(emptyArticleEntityOptional);
      when(articleEntityMapper.toArticleOptional(emptyArticleEntityOptional)).thenReturn(Optional.empty());

      Optional<Article> result = articleAdapter.getArticleById(idArticle);

      assertFalse(result.isPresent());

      verify(iArticleRepository).findById(idArticle);
      verify(articleEntityMapper).toArticleOptional(emptyArticleEntityOptional);
   }


}