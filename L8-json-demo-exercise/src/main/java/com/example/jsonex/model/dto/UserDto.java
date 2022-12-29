package com.example.jsonex.model.dto;

import com.google.gson.annotations.Expose;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class UserDto {
    @Expose
    private String firstName;
    @Expose
    private String lastName;
    @Expose
    private Integer age;
    @Expose
    private Set<ProductDto> sellingProducts;
    @Expose
    private Set<ProductDto> boughtProducts;
    @Expose
    private Set<UserDto> friends;

    public String getFullName() {
        return firstName + " " + lastName;
    }

    public UserWithProductsDto toUserWithProductsDto() {
        return new UserWithProductsDto(firstName, lastName, age, toProductsSoldWithCountDto());
    }

    public ProductsSoldWithCountDto  toProductsSoldWithCountDto() {
        return new ProductsSoldWithCountDto(sellingProducts
                .stream()
                .map(this::toProductsBasicInfo)
                .collect(Collectors.toList()));
    }

    public ProductsBasicInfo toProductsBasicInfo(ProductDto productDto) {
        return new ProductsBasicInfo(productDto.getName(), productDto.getPrice());
    }

    public UserDto() {
        this.sellingProducts = new HashSet<>();
        this.boughtProducts = new HashSet<>();
        this.friends = new HashSet<>();
    }

    public UserDto(String firstName, String lastName, Integer age, Set<ProductDto> sellingProducts, Set<ProductDto> boughtProducts, Set<UserDto> friends) {
        this.firstName = firstName;
        this.lastName = lastName;
        this.age = age;
        this.sellingProducts = sellingProducts;
        this.boughtProducts = boughtProducts;
        this.friends = friends;
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

    public Set<ProductDto> getSellingProducts() {
        return sellingProducts;
    }

    public void setSellingProducts(Set<ProductDto> sellingProducts) {
        this.sellingProducts = sellingProducts;
    }

    public Set<ProductDto> getBoughtProducts() {
        return boughtProducts;
    }

    public void setBoughtProducts(Set<ProductDto> boughtProducts) {
        this.boughtProducts = boughtProducts;
    }

    public Set<UserDto> getFriends() {
        return friends;
    }

    public void setFriends(Set<UserDto> friends) {
        this.friends = friends;
    }
}
