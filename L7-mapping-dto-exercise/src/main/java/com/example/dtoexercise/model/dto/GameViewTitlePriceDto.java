package com.example.dtoexercise.model.dto;

import java.math.BigDecimal;

public class GameViewTitlePriceDto {

    private String title;
    private BigDecimal price;

    public GameViewTitlePriceDto() {

    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }
}
