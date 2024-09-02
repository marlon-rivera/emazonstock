package com.emazon.stock.adapters.driven.jpa.mysql.entity;

import com.emazon.stock.utils.Constants;
import jakarta.persistence.*;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.HashSet;
import java.util.Set;

@Entity(name = "articles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ArticleEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    @Column(nullable = false)
    private String name;
    @Column(nullable = false)
    private String description;
    @Column(nullable = false)
    private int quantity;
    @Column(nullable = false)
    private BigDecimal price;
    @ManyToOne(optional = false)
    @JoinColumn(name = "brandId")
    private BrandEntity brand;
    @ManyToMany(fetch = FetchType.LAZY)
    @JoinTable(
            name = "article_categories",
            joinColumns = @JoinColumn(name = "articleId"),
            inverseJoinColumns = @JoinColumn(name = "categorieId")
    )
    @Size(min = Constants.MIN_CATEGORIES_BY_ARTICLE, max = Constants.MAX_CATEGORIES_BY_ARTICLE)
    private Set<CategoryEntity> categories = new HashSet<>();
}
