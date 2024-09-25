package com.emazon.stock.domain.api.usecase;

import com.emazon.stock.domain.api.IBrandServicePort;
import com.emazon.stock.domain.api.ICategoryServicePort;
import com.emazon.stock.domain.exception.article.ArticleExceedsCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleMinimumCategoriesException;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
import com.emazon.stock.domain.exception.article.ArticleWithRepeatedCategoriesException;
import com.emazon.stock.domain.exception.brand.BrandNoDataFoundException;
import com.emazon.stock.domain.exception.category.CategoryNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.Category;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.domain.spi.IArticlePersistencePort;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.assertEquals;
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

    @Test
    void testSaveArticleWithCategoriesThatNotExistsShouldFail() {
        Category category = new Category(1L, "name", "description");
        Category category1 = new Category(1L, "", "");
        Brand brand = new Brand(1L, "name", "description");
        when(brandServicePort.getBrandById(1L)).thenReturn(Optional.of(brand));
        Set<Category> categories = new HashSet<>();
        categories.add(category);
        categories.add(category1);
        System.out.println(categories.size());
        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, brand);
        article.setCategories(categories);
        assertThrows(CategoryNoDataFoundException.class, () -> articleUseCase.saveArticle(article));
    }

    @Test
    void getAllArticles_ShouldThrowArticleNoDataFoundException_WhenNoArticlesFound() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortDirection = "asc";
        List<Long> idsCategories = List.of(1L);

        PaginationInfo<Article> emptyPaginationInfo = new PaginationInfo<>(
              List.of(),
                page,
                size,
                10,
                1,
                false,
                false
        );

        when(articlePersistencePort.getAllArticles(page, size, sortBy, sortDirection, idsCategories)).thenReturn(emptyPaginationInfo);

        assertThrows(ArticleNoDataFoundException.class, () -> articleUseCase.getAllArticles(page, size, sortBy, sortDirection, idsCategories));
    }

    @Test
    void getAllArticles_ShouldReturnArticles_WhenArticlesAreFound() {
        int page = 0;
        int size = 10;
        String sortBy = "name";
        String sortDirection = "asc";
        List<Long> idsCategories = List.of(1L);

        Article article = new Article(
                1L,
                "Name",
                "Description",
                10,
                new BigDecimal(10),
                new Brand(1L, "name", "description")
        );
        article.setCategories(Set.of(new Category(1L, "name", "description")));

        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(article),
                1,
                1,
                1,
                1,
                false,
                false
        );


        when(articlePersistencePort.getAllArticles(page, size, sortBy, sortDirection, idsCategories)).thenReturn(paginationInfo);

        PaginationInfo<Article> result = articleUseCase.getAllArticles(page, size, sortBy, sortDirection, idsCategories);

        assertEquals(1, result.getList().size());
    }

    @Test
    void increaseStockArticleShouldUpdateStock() {
        Article article = new Article(
                1L,
                "Name",
                "Description",
                10,
                new BigDecimal(10),
                new Brand(1L, "name", "description")
        );

        BigInteger quantityToAdd = BigInteger.valueOf(5);
        Article updatedArticle = new Article(article.getId(), article.getName(), article.getDescription(), article.getQuantity() + quantityToAdd.intValue(), article.getPrice(), article.getBrand());
        updatedArticle.setCategories(article.getCategories());

        when(articlePersistencePort.getArticleById(article.getId())).thenReturn(Optional.of(article));

        articleUseCase.increaseStockArticle(article.getId(), quantityToAdd);

        ArgumentCaptor<Article> articleCaptor = ArgumentCaptor.forClass(Article.class);
        verify(articlePersistencePort).increaseStockArticle(articleCaptor.capture());
        Article capturedArticle = articleCaptor.getValue();
        
        assertEquals(updatedArticle.getId(), capturedArticle.getId());
        assertEquals(updatedArticle.getName(), capturedArticle.getName());
        assertEquals(updatedArticle.getDescription(), capturedArticle.getDescription());
        assertEquals(updatedArticle.getQuantity(), capturedArticle.getQuantity());
        assertEquals(updatedArticle.getPrice(), capturedArticle.getPrice());
        assertEquals(updatedArticle.getBrand(), capturedArticle.getBrand());
        assertEquals(updatedArticle.getCategories(), capturedArticle.getCategories());
    }

    @Test
    void increaseStockArticleShouldThrowExceptionWhenArticleNotFound() {
        Long articleId = 1L;
        BigInteger quantityToAdd = BigInteger.valueOf(5);

        when(articlePersistencePort.getArticleById(articleId)).thenReturn(Optional.empty());

        assertThrows(ArticleNoDataFoundException.class, () -> articleUseCase.increaseStockArticle(articleId, quantityToAdd));
    }

    @Test
    void getQuantityArticle_ShouldReturnQuantity_WhenArticleExists() {
        Long idArticle = 1L;
        BigInteger expectedQuantity = BigInteger.valueOf(100);

        when(articlePersistencePort.getQuantityArticle(idArticle)).thenReturn(expectedQuantity);

        BigInteger actualQuantity = articleUseCase.getQuantityArticle(idArticle);

        assertEquals(expectedQuantity, actualQuantity);
    }

    @Test
    void getCategoriesIds_ShouldReturnCategoryIds_WhenArticleExists() {
        Long idArticle = 1L;
        Article article = new Article(1L, "Test", "Test", 10, BigDecimal.ONE, new Brand(1L, "name", "description"));
        Category category1 = new Category(1L, "Category1", "Description");
        Category category2 = new Category(2L, "Category2", "Description");
        article.setCategories(Set.of(category1, category2));

        when(articlePersistencePort.getArticleById(idArticle)).thenReturn(Optional.of(article));

        List<Long> actualCategoryIds = articleUseCase.getCategoriesIds(idArticle);
        List<Long> expectedCategoryIds = List.of(1L, 2L);
        List<Long> sortedExpected = expectedCategoryIds.stream().sorted().toList();
        List<Long> sortedActual = actualCategoryIds.stream().sorted().toList();
        assertEquals(sortedExpected, sortedActual);
    }

    @Test
    void getCategoriesIds_ShouldThrowArticleNoDataFoundException_WhenArticleDoesNotExist() {
        Long idArticle = 999L;

        when(articlePersistencePort.getArticleById(idArticle)).thenReturn(Optional.empty());

        assertThrows(ArticleNoDataFoundException.class, () -> articleUseCase.getCategoriesIds(idArticle));
    }

    @Test
    void testGetArticlesOfShoppingCart_withCategoriesAndBrands() {
        int page = 0;
        int size = 10;
        String order = "ASC";
        List<Long> idsArticles = List.of(1L, 2L, 3L);
        List<Long> idsCategories = List.of(1L, 2L);
        List<Long> idsBrands = List.of(1L, 2L);

        PaginationInfo<Article> expectedPaginationInfo = new PaginationInfo<>(List.of(), 1, 0, 0, 1, false, false);

        when(articlePersistencePort.findByIdsAndCategoriesAndBrands(page, size, idsArticles, order, idsCategories, idsBrands))
                .thenReturn(expectedPaginationInfo);

        PaginationInfo<Article> result = articleUseCase.getArticlesOfShoppingCart(page, size, idsArticles, order, idsCategories, idsBrands);

        assertEquals(expectedPaginationInfo, result);

        verify(articlePersistencePort, times(1)).findByIdsAndCategoriesAndBrands(page, size, idsArticles, order, idsCategories, idsBrands);
    }

    @Test
    void testGetArticlesOfShoppingCart_withOnlyBrands() {

        int page = 0;
        int size = 10;
        String order = "ASC";
        List<Long> idsArticles = List.of(1L, 2L, 3L);
        List<Long> idsCategories = List.of();
        List<Long> idsBrands = List.of(1L, 2L);

        PaginationInfo<Article> expectedPaginationInfo = new PaginationInfo<>(List.of(), 1, 0, 0, 1, false, false);

        when(articlePersistencePort.findByBrandIdsAndIds(page, size, idsArticles, order, idsBrands))
                .thenReturn(expectedPaginationInfo);

        PaginationInfo<Article> result = articleUseCase.getArticlesOfShoppingCart(page, size, idsArticles, order, idsCategories, idsBrands);

        assertEquals(expectedPaginationInfo, result);

        verify(articlePersistencePort, times(1)).findByBrandIdsAndIds(page, size, idsArticles, order, idsBrands);
    }

    @Test
     void testGetArticlesOfShoppingCart_withOnlyCategories() {
        int page = 0;
        int size = 10;
        String order = "ASC";
        List<Long> idsArticles = List.of(1L, 2L, 3L);
        List<Long> idsCategories = List.of(1L, 2L);
        List<Long> idsBrands = List.of();

        PaginationInfo<Article> expectedPaginationInfo = new PaginationInfo<>(List.of(), 1, 0, 0, 1, false, false);

        when(articlePersistencePort.findByCategoriesIdsAndIds(page, size, idsArticles, order, idsCategories))
                .thenReturn(expectedPaginationInfo);

        PaginationInfo<Article> result = articleUseCase.getArticlesOfShoppingCart(page, size, idsArticles, order, idsCategories, idsBrands);

        assertEquals(expectedPaginationInfo, result);

        verify(articlePersistencePort, times(1)).findByCategoriesIdsAndIds(page, size, idsArticles, order, idsCategories);
    }

    @Test
    void testGetArticlesOfShoppingCart_withOnlyIdsArticles() {
        int page = 0;
        int size = 10;
        String order = "ASC";
        List<Long> idsArticles = List.of(1L, 2L, 3L);
        List<Long> idsCategories = List.of();
        List<Long> idsBrands = List.of();

        PaginationInfo<Article> expectedPaginationInfo = new PaginationInfo<>(List.of(), 1, 0, 0, 1, false, false);

        when(articlePersistencePort.findByIds(page, size, idsArticles, order))
                .thenReturn(expectedPaginationInfo);

        PaginationInfo<Article> result = articleUseCase.getArticlesOfShoppingCart(page, size, idsArticles, order, idsCategories, idsBrands);

        assertEquals(expectedPaginationInfo, result);

        verify(articlePersistencePort, times(1)).findByIds(page, size, idsArticles, order);
    }
}