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

}
