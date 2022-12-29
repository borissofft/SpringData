package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class CategoryProductSummaryDto {
    @Expose
    private String category;
    @Expose
    private Long productsCount;
    @Expose
    private double averagePrice;
    @Expose
    private BigDecimal totalRevenue;

    public CategoryProductSummaryDto() {

    }

    public CategoryProductSummaryDto(String category, Long productsCount, double averagePrice, BigDecimal totalRevenue) {
        this.category = category;
        this.productsCount = productsCount;
        this.averagePrice = averagePrice;
        this.totalRevenue = totalRevenue;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public Long getProductsCount() {
        return productsCount;
    }

    public void setProductsCount(Long productsCount) {
        this.productsCount = productsCount;
    }

    public double getAveragePrice() {
        return averagePrice;
    }

    public void setAveragePrice(double averagePrice) {
        this.averagePrice = averagePrice;
    }

    public BigDecimal getTotalRevenue() {
        return totalRevenue;
    }

    public void setTotalRevenue(BigDecimal totalRevenue) {
        this.totalRevenue = totalRevenue;
    }
}
