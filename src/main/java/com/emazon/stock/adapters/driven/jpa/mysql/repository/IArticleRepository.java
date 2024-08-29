package com.emazon.stock.adapters.driven.jpa.mysql.repository;

import com.emazon.stock.adapters.driven.jpa.mysql.entity.ArticleEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface IArticleRepository extends JpaRepository<ArticleEntity, Long> {



}
