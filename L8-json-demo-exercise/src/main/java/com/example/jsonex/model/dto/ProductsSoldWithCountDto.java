package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

public class ProductsSoldWithCountDto {

    @Expose
    private Integer count;
    @Expose
    private List<ProductsBasicInfo> products;

    public ProductsSoldWithCountDto() {

    }

    public ProductsSoldWithCountDto(List<ProductsBasicInfo> products) {
        this.products = products;
        this.count = products.size();
    }

    public ProductsSoldWithCountDto(Integer count, List<ProductsBasicInfo> products) {
        this.count = count;
        this.products = products;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public List<ProductsBasicInfo> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsBasicInfo> products) {
        this.products = products;
    }
}
