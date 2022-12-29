package com.example.xmlex.service.impl;

import com.example.xmlex.model.dto.CategoryProductSummaryDto;
import com.example.xmlex.model.dto.ProductSeedDto;
import com.example.xmlex.model.dto.ProductViewRootDto;
import com.example.xmlex.model.dto.ProductWithSellerDto;
import com.example.xmlex.model.entity.Product;
import com.example.xmlex.repository.ProductRepository;
import com.example.xmlex.service.CategoryService;
import com.example.xmlex.service.ProductService;
import com.example.xmlex.service.UserService;
import com.example.xmlex.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductServiceImpl implements ProductService {

    private final ProductRepository productRepository;
    private final UserService userService;
    private final CategoryService categoryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public ProductServiceImpl(ProductRepository productRepository, UserService userService, CategoryService categoryService, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.productRepository = productRepository;
        this.userService = userService;
        this.categoryService = categoryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public long getEntityCount() {
        return this.productRepository.count();
    }

    @Override
    public void sedProducts(List<ProductSeedDto> products) { // Here we fill products table and fill mapping table products_categories!!!

        products.stream()
                .filter(validationUtil::isValid)
                .map(productSeedDto -> {
                    Product product = modelMapper.map(productSeedDto, Product.class);
                    product.setSeller(this.userService.getRandomUser()); // make relation seller_id - id(user) - mandatory

                    if (product.getPrice().compareTo(BigDecimal.valueOf(700L)) > 0) {
                        product.setBuyer(this.userService.getRandomUser()); // make relation buyer_id - id(user) - optional
                    }

                    product.setCategories(this.categoryService.getRandomCategories()); // add(fill table) mapping table relations!
                    return product;
                })
                .forEach(productRepository::save);

    }

    @Override
    public ProductViewRootDto findProductsInRangeWithoutBuyer() {
        ProductViewRootDto rootDto = new ProductViewRootDto();

        rootDto
                .setProducts(this.productRepository
                        .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L))
                        .stream()
                        .map(product -> {
                            ProductWithSellerDto productWithSellerDto = modelMapper
                                    .map(product, ProductWithSellerDto.class);
                            String firstName = product.getSeller().getFirstName();
                            String lastName = product.getSeller().getLastName();
                            if (firstName == null) { // Only firstName can be null by task condition
                                productWithSellerDto.setSeller(lastName);
                            } else {
                                productWithSellerDto.setSeller(String.format("%s %s", firstName, lastName));
                            }
                            return productWithSellerDto;
                        })
                        .collect(Collectors.toList()));

        return rootDto;
    }

    @Override
    public CategoryProductSummaryDto getSummaryFromCategories() {
        CategoryProductSummaryDto summaryDto = new CategoryProductSummaryDto();
        summaryDto.setCategory(this.productRepository.getSummaryCategoriesByProductsCount());
        return summaryDto;
    }

}
