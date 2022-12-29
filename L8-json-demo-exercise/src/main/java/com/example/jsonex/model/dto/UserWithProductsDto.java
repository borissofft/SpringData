package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.util.List;

public class UserWithProductsDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Integer age;
    @Expose
    private ProductsSoldWithCountDto products;

    public UserWithProductsDto() {

    }

    public UserWithProductsDto(String firstName, String lastName, Integer age, ProductsSoldWithCountDto products) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.products = products;
    }

    public String getFirstName() {
        return firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public Integer getAge() {
        return age;
    }

    public void setAge(Integer age) {
        this.age = age;
    }

    public ProductsSoldWithCountDto getProducts() {
        return products;
    }

    public void setProducts(ProductsSoldWithCountDto products) {
        this.products = products;
    }
}
