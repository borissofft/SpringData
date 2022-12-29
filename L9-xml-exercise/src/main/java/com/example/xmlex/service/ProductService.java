package com.example.xmlex.service;

import com.example.xmlex.model.dto.CategoryProductSummaryDto;
import com.example.xmlex.model.dto.ProductSeedDto;
import com.example.xmlex.model.dto.ProductViewRootDto;

import java.math.BigDecimal;
import java.util.List;

public interface ProductService {
    long getEntityCount();
    void sedProducts(List<ProductSeedDto> products);

    ProductViewRootDto findProductsInRangeWithoutBuyer();

    CategoryProductSummaryDto getSummaryFromCategories();
}
