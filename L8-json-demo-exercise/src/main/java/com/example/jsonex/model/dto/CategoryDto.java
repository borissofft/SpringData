package com.example.jsonex.model.dto;

import com.example.jsonex.model.entity.Product;
import com.google.gson.annotations.Expose;

import java.util.Set;

public class CategoryDto {

    @Expose
    private String name;

    @Expose
    private Set<Product> products;

    public CategoryDto() {

    }

    public CategoryDto(String name, Set<Product> products) {
        this.name = name;
        this.products = products;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return products;
    }

    public void setProducts(Set<Product> products) {
        this.products = products;
    }
}
