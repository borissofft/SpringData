package com.example.jsonex.service;

import com.example.jsonex.model.dto.CategoryProductSummaryDto;
import com.example.jsonex.model.dto.ProductNameAndPriceDto;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.List;

public interface ProductService {

    void seedProducts() throws IOException;

    List<ProductNameAndPriceDto> findAllByPriceBetweenAndBuyerIsNull(BigDecimal lowed, BigDecimal upper);

    List<CategoryProductSummaryDto> getCategorySummary();
}
