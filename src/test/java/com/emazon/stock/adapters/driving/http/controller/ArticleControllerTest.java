package com.emazon.stock.adapters.driving.http.controller;

import com.emazon.stock.adapters.driving.http.dto.request.ArticleRequest;
import com.emazon.stock.adapters.driving.http.dto.request.BrandRequest;
import com.emazon.stock.adapters.driving.http.dto.response.ArticleResponse;
import com.emazon.stock.adapters.driving.http.dto.response.PaginationInfoResponse;
import com.emazon.stock.adapters.driving.http.mapper.request.IArticleRequestMapper;
import com.emazon.stock.adapters.driving.http.mapper.response.IArticleResponseMapper;
import com.emazon.stock.configuration.jwt.JWTAuthFilter;
import com.emazon.stock.domain.api.IArticleServicePort;
import com.emazon.stock.domain.exception.article.ArticleNoDataFoundException;
import com.emazon.stock.domain.model.Article;
import com.emazon.stock.domain.model.Brand;
import com.emazon.stock.domain.model.PaginationInfo;
import com.emazon.stock.utils.Constants;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = ArticleController.class)
@ExtendWith(MockitoExtension.class)
@AutoConfigureMockMvc(addFilters = false)
class ArticleControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private IArticleServicePort articleServicePort;

    @MockBean
    private IArticleRequestMapper iArticleRequestMapper;

    @MockBean
    private IArticleResponseMapper articleResponseMapper;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JWTAuthFilter jwtAuthFilter;

    @Test
    void testSaveArticle() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest(
                "name", "description", 1, new BigDecimal(10), List.of(1L), new BrandRequest(1L, "name", "description"));

        Article article = new Article(1L, "name", "description", 1, BigDecimal.ONE, new Brand(1L, "name", "description"));

        when(iArticleRequestMapper.toArticle(any(ArticleRequest.class))).thenReturn(article);
        doNothing().when(articleServicePort).saveArticle(any(Article.class));

        mockMvc.perform(post("/article/")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleRequest))
        ).andExpect(status().isCreated());

        verify(iArticleRequestMapper).toArticle(any(ArticleRequest.class));
        verify(articleServicePort).saveArticle(any(Article.class));
    }

    @Test
    void testSaveArticleWithoutCategoriesShouldReturnBadRequest() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest(
                "name", "description", 1, new BigDecimal(10), List.of(), new BrandRequest(1L, "name", "description"));

        mockMvc.perform(post("/article/")
        .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(articleRequest))
        ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_ARTICLE_MINIMUM_CATEGORIES)));
    }

    @Test
    void testSaveArticleWithCategoriesRepeatedShouldReturnBadRequest() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest(
                "name", "description", 1, new BigDecimal(10), List.of(1L, 1L), new BrandRequest(1L, "name", "description"));

        mockMvc.perform(post("/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.EXCEPTION_ARTICLE_WITH_REPEATED_CATEGORIES)));
    }

    @Test
    void testSaveArticleWithoutBrandShouldReturnBadRequest() throws Exception {
        ArticleRequest articleRequest = new ArticleRequest(
                "name", "description", 1, new BigDecimal(10), List.of(1L), null);

        mockMvc.perform(post("/article/")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(articleRequest))
                ).andExpect(status().isBadRequest())
                .andExpect(result -> assertTrue(Objects.requireNonNull(result.getResolvedException()).getMessage().contains(Constants.ARTICLE_BRAND_MUST_MANDATORY)));
    }

    @Test
    void testGetAllArticlesOrderByArticleNameAsc() throws Exception {
        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(articleServicePort.getAllArticles(anyInt(), anyInt(), anyString(), anyString(), anyList())).thenReturn(paginationInfo);

        PaginationInfoResponse<ArticleResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(articleResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/article/all")
                .param("page", "0")
                .param("size", "10")
                .param("order", "ASC")
                .param("sortBy", "name")
                .param("idsCategories", "1","2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllArticlesOrderByArticleNameDesc() throws Exception {
        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(articleServicePort.getAllArticles(anyInt(), anyInt(), anyString(), anyString(), anyList())).thenReturn(paginationInfo);

        PaginationInfoResponse<ArticleResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(articleResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/article/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "DESC")
                        .param("sortBy", "name")
                        .param("idsCategories", "1","2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));
    }

    @Test
    void testGetAllArticlesOrderByBrandNameAsc() throws Exception {
        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(articleServicePort.getAllArticles(anyInt(), anyInt(), anyString(), anyString(), anyList())).thenReturn(paginationInfo);

        PaginationInfoResponse<ArticleResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(articleResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/article/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "ASC")
                        .param("sortBy", "brand")
                        .param("idsCategories", "1","2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void testGetAllArticlesOrderByBrandNameDesc() throws Exception {
        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(articleServicePort.getAllArticles(anyInt(), anyInt(), anyString(), anyString(), anyList())).thenReturn(paginationInfo);

        PaginationInfoResponse<ArticleResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(articleResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/article/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "DESC")
                        .param("sortBy", "brand")
                        .param("idsCategories", "1","2"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void testGetAllArticlesWithoutIdsCategories() throws Exception {
        PaginationInfo<Article> paginationInfo = new PaginationInfo<>(
                List.of(),
                0,
                10,
                2,
                1,
                false,
                false
        );
        when(articleServicePort.getAllArticles(anyInt(), anyInt(), anyString(), anyString(), anyList())).thenReturn(paginationInfo);

        PaginationInfoResponse<ArticleResponse> paginationInfoResponse = new PaginationInfoResponse<>();
        when(articleResponseMapper.toPaginationInfoResponse(paginationInfo)).thenReturn(paginationInfoResponse);

        mockMvc.perform(get("/article/all")
                        .param("page", "0")
                        .param("size", "10")
                        .param("order", "DESC")
                        .param("sortBy", "brand"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON));

    }

    @Test
    void testIncreaseArticleStockSuccess() throws Exception {
        Long articleId = 1L;
        BigInteger quantity = BigInteger.TEN;

        doNothing().when(articleServicePort).increaseStockArticle(articleId, quantity);

        mockMvc.perform(put("/article/increase/{id}", articleId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(quantity)))
                .andExpect(status().isOk());

        verify(articleServicePort).increaseStockArticle(articleId, quantity);
    }

    @Test
    void getArticleQuantity_ShouldReturnQuantity_WhenArticleExists() throws Exception {
        Long articleId = 1L;
        BigInteger quantity = new BigInteger("100");

        when(articleServicePort.getQuantityArticle(articleId)).thenReturn(quantity);

        mockMvc.perform(get("/article/quantity/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().string(quantity.toString()));

        verify(articleServicePort).getQuantityArticle(articleId);
    }

    @Test
    void getArticleQuantity_ShouldReturnNotFound_WhenArticleDoesNotExist() throws Exception {
        Long articleId = 1L;

        when(articleServicePort.getQuantityArticle(articleId)).thenThrow(ArticleNoDataFoundException.class);

        mockMvc.perform(get("/article/quantity/{id}", articleId))
                .andExpect(status().isNotFound());

        verify(articleServicePort).getQuantityArticle(articleId);
    }

    @Test
    void getCategoriesIds_ShouldReturnCategories_WhenArticleExists() throws Exception {
        Long articleId = 1L;
        List<Long> categoriesIds = Arrays.asList(1L, 2L, 3L);

        when(articleServicePort.getCategoriesIds(articleId)).thenReturn(categoriesIds);

        mockMvc.perform(get("/article/categories/{id}", articleId))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(content().json("[1,2,3]"));

        verify(articleServicePort).getCategoriesIds(articleId);
    }

    @Test
    void getCategoriesIds_ShouldReturnNotFound_WhenArticleDoesNotExist() throws Exception {
        Long articleId = 1L;

        when(articleServicePort.getCategoriesIds(articleId)).thenThrow(ArticleNoDataFoundException.class);

        mockMvc.perform(get("/article/categories/{id}", articleId))
                .andExpect(status().isNotFound());

        verify(articleServicePort).getCategoriesIds(articleId);
    }
}