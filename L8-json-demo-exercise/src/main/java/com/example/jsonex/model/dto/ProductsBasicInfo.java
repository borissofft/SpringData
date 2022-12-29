package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.math.BigDecimal;

public class ProductsBasicInfo {

    @Expose
    private String name;
    @Expose
    private BigDecimal price;

    public ProductsBasicInfo() {

    }
    public ProductsBasicInfo(String name, BigDecimal price) {
        this.name = name;
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
