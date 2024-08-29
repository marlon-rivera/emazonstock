package com.emazon.stock.domain.model;

import java.util.HashSet;
import java.util.Set;

public class Category {

    private Long id;
    private String name;
    private String description;
    private Set<Article> articles = new HashSet<Article>();

    public Category(Long id, String name, String description){
        this.id = id;
        this.name = name;
        this.description = description;
    }

    public void addArticle(Article article){
        articles.add(article);
    }

    public Set<Article> getArticles() {
        return articles;
    }

    public void setArticles(Set<Article> articles) {
        this.articles = articles;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Long getId() {
        return id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

}
