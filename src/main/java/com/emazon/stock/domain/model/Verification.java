package com.emazon.stock.domain.model;

import java.math.BigDecimal;

public class Verification {

    private Long idArticle;
    private int quantityStock;
    private int quantityRequired;
    private String status;
    private BigDecimal unitPrice;

    public Verification(Long idArticle, int quantityStock, int quantityRequired, String status, BigDecimal unitPrice) {
        this.idArticle = idArticle;
        this.quantityStock = quantityStock;
        this.quantityRequired = quantityRequired;
        this.status = status;
        this.unitPrice = unitPrice;
    }

    public Long getIdArticle() {
        return idArticle;
    }

    public void setIdArticle(Long idArticle) {
        this.idArticle = idArticle;
    }

    public int getQuantityRequired() {
        return quantityRequired;
    }

    public void setQuantityRequired(int quantityRequired) {
        this.quantityRequired = quantityRequired;
    }

    public int getQuantityStock() {
        return quantityStock;
    }

    public void setQuantityStock(int quantityStock) {
        this.quantityStock = quantityStock;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getStatus() {
        return status;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(BigDecimal unitPrice) {
        this.unitPrice = unitPrice;
    }
}
