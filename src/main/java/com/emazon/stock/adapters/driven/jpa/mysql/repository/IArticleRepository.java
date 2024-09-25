package com.emazon.stock.adapters.driven.jpa.mysql.repository;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface IArticleRepository extends JpaRepository<ArticleEntity, Long> {

    @Query("SELECT a FROM articles a JOIN a.categories c WHERE c.id IN :categoriesIds")
    Page<ArticleEntity> findByCategoriaIds(@Param("categoriesIds") List<Long> categoriesIds, Pageable pageable);

    @Query("SELECT a FROM articles a WHERE a.id IN :idsArticles")
    Page<ArticleEntity> findByIds(@Param("idsArticles") List<Long> idsArticles, Pageable pageable);


    @Query("SELECT a FROM articles a " +
            "JOIN a.categories c " +
            "WHERE a.id IN :idsArticles " +
            "AND c.id IN :idsCategories " +
            "AND a.brand.id IN :idsBrands")
    Page<ArticleEntity> findByIdsAndCategoriesAndBrands(@Param("idsArticles") List<Long> idsArticles,
                                                        @Param("idsCategories") List<Long> idsCategories,
                                                        @Param("idsBrands") List<Long> idsBrands,
                                                        Pageable pageable);



    @Query("SELECT a FROM articles a " +
            "JOIN a.categories c " +
            "WHERE c.id IN :idsCategories " +
            "AND a.id IN :idsArticles")
    Page<ArticleEntity> findByCategoriesIdsAndIds(@Param("idsCategories") List<Long> idsCategories,
                                                  @Param("idsArticles") List<Long> idsArticles,
                                                  Pageable pageable);



    @Query("SELECT a FROM articles a WHERE a.brand.id IN :idsBrands AND a.id IN :idsArticles")
    Page<ArticleEntity> findByBrandIdsAndIds(@Param("idsBrands") List<Long> idsBrands,
                                             @Param("idsArticles") List<Long> idsArticles,
                                             Pageable pageable);


}
