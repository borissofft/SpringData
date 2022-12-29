package com.example.jsonex.model.dto;

import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

public class ProductDto {

    private String name;
    private BigDecimal price;
    private UserDto buyer;
    private UserDto seller;
    private Set<CategoryDto> categories;

    public ProductDto() {

    }

    public ProductDto(String name, BigDecimal price, UserDto buyer, UserDto seller, Set<CategoryDto> categories) {
        this.name = name;
        this.price = price;
        this.buyer = buyer;
        this.seller = seller;
        this.categories = categories;
    }

//    public ProductInRangeWithNoBuyerDto toProductInRangeWithNoBuyerDto() {
//        return new ProductInRangeWithNoBuyerDto(name, price, seller.getFullName());
//    }


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

    public UserDto getBuyer() {
        return buyer;
    }

    public void setBuyer(UserDto buyer) {
        this.buyer = buyer;
    }

    public UserDto getSeller() {
        return seller;
    }

    public void setSeller(UserDto seller) {
        this.seller = seller;
    }

    public Set<CategoryDto> getCategories() {
        return categories;
    }

    public void setCategories(Set<CategoryDto> categories) {
        this.categories = categories;
    }
}
