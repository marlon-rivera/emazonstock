package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.exception.article.ArticleExceedsCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleMinimumCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleWithRepeatedCategoriesException;
import com.emazon.stock.domain.exception.brand.BrandNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.*;

class ArticleUseCaseImplTest {

    @Mock
    private IArticlePersistencePort articlePersistencePort;

    @Mock
    private ICategoryServicePort categoryServicePort;

    @Mock
    private IBrandServicePort brandServicePort;

    @InjectMocks
    private ArticleUseCaseImpl articleUseCase;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testSaveArticleSuccessfully(){
        Category category = new Category(1L, "name", "description");
        when(categoryServicePort.getCategoryById(1L)).thenReturn(Optional.of(category));

        Brand brand = new Brand(1L, "name", "description");
        when(brandServicePort.getBrandById(1L)).thenReturn(Optional.of(brand));

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);
        article.setCategories(categories);

        articleUseCase.saveArticle(article);

        verify(articlePersistencePort, times(1)).saveArticle(any(Article.class));
    }

    @Test
    void testSaveArticleWithoutCategoriesShouldFail(){
        Brand brand = new Brand(1L, "name", "description");
        when(brandServicePort.getBrandById(1L)).thenReturn(Optional.of(brand));

        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);

        assertThrows(ArticleMinimumCategoriesException.class, () -> articleUseCase.saveArticle(article));
    }

    @Test
    void testSaveArticleThatExceedsCategoriesShouldFail(){
        Brand brand = new Brand(1L, "name", "description");
        when(brandServicePort.getBrandById(1L)).thenReturn(Optional.of(brand));
        Category category = new Category(1L, "name", "description");
        when(categoryServicePort.getCategoryById(1L)).thenReturn(Optional.of(category));
        Category category2 = new Category(2L, "name", "description");
        when(categoryServicePort.getCategoryById(2L)).thenReturn(Optional.of(category2));
        Category category3 = new Category(3L, "name", "description");
        when(categoryServicePort.getCategoryById(3L)).thenReturn(Optional.of(category3));
        Category category4 = new Category(4L, "name", "description");
        when(categoryServicePort.getCategoryById(4L)).thenReturn(Optional.of(category4));


        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category2);
        categories.add(category3);
        categories.add(category4);

        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);
        article.setCategories(categories);

        assertThrows(ArticleExceedsCategoriesException.class, () -> articleUseCase.saveArticle(article));
    }

    @Test
    void testSaveArticleWithoutBrandShouldFail(){
        Category category = new Category(1L, "name", "description");
        when(categoryServicePort.getCategoryById(1L)).thenReturn(Optional.of(category));

        Brand brand = new Brand(0L, "", "");
        when(brandServicePort.getBrandById(0L)).thenReturn(Optional.empty());

        Set<Category> categories = new HashSet<>();
        categories.add(category);

        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);
        article.setCategories(categories);

        assertThrows(BrandNoDataFoundException.class, () -> articleUseCase.saveArticle(article));
    }

    @Test
    void testSaveArticleWitCategoriesRepeatedShouldFail(){
        Category category = new Category(1L, "name", "description");
        Category category1 = new Category(1L, "", "");
        when(categoryServicePort.getCategoryById(1L)).thenReturn(Optional.of(category));
        Brand brand = new Brand(1L, "name", "description");
        when(brandServicePort.getBrandById(1L)).thenReturn(Optional.of(brand));
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category1);
        System.out.println(categories.size());
        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);
        article.setCategories(categories);
        assertThrows(ArticleWithRepeatedCategoriesException.class, () -> articleUseCase.saveArticle(article));
    }


}