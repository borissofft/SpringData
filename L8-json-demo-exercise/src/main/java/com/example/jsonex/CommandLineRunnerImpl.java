package com.example.jsonex;

import com.example.jsonex.model.dto.CategoryProductSummaryDto;
import com.example.jsonex.model.dto.ProductNameAndPriceDto;
import com.example.jsonex.model.dto.UserSoldDto;
import com.example.jsonex.model.dto.UsersWithProductWrapperDto;
import com.example.jsonex.service.CategoryService;
import com.example.jsonex.service.ProductService;
import com.example.jsonex.service.UserService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUTPUT_PATH = "src/main/resources/files/out/";
    private static final String PRODUCT_IN_RANGE_FILE_NAME = "products-in-range.json";
    private static final String USERS_SOLD_PRODUCTS_FILE_NAME = "users-sold-products.json";
    private static final String CATEGORIES_BY_PRODUCTS_FILE_NAME = "categories-by-products.json";
    private static final String USERS_AND_PRODUCTS_FILE_NAME = "users-and-products.json";
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;
    private final BufferedReader reader;
    private final Gson gson;

    @Autowired
    public CommandLineRunnerImpl(CategoryService categoryService, UserService userService, ProductService productService, Gson gson) {
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
        this.gson = gson;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise number: ");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 1 -> productsInRange();
            case 2 -> soldProducts();
            case 3 -> categoriesByProductsCount();
            case 4 -> usersAndProducts();
        }


    }

    private void usersAndProducts() throws IOException {

        UsersWithProductWrapperDto usersWithProductWrapperDtos = this.userService
                .findAllUsersAndProducts();

        String content = this.gson.toJson(usersWithProductWrapperDtos);
        writeToFile(OUTPUT_PATH + USERS_AND_PRODUCTS_FILE_NAME, content);
    }

    private void categoriesByProductsCount() throws IOException {
        List<CategoryProductSummaryDto> categoryProductSummaryDtos = this.productService
                .getCategorySummary();

        String content = this.gson.toJson(categoryProductSummaryDtos);

        writeToFile(OUTPUT_PATH + CATEGORIES_BY_PRODUCTS_FILE_NAME, content);
    }

    private void soldProducts() throws IOException {

     List<UserSoldDto> userSoldDtos =  this.userService
                .findAllUsersWithMoreThanOneSoldProducts();

     String content = this.gson.toJson(userSoldDtos);

     writeToFile(OUTPUT_PATH + USERS_SOLD_PRODUCTS_FILE_NAME, content);

    }

    private void productsInRange() throws IOException {

        List<ProductNameAndPriceDto> productDtos = this.productService
                .findAllByPriceBetweenAndBuyerIsNull(BigDecimal.valueOf(500L), BigDecimal.valueOf(1000L));

        String content = this.gson.toJson(productDtos);

        writeToFile(OUTPUT_PATH + PRODUCT_IN_RANGE_FILE_NAME, content);

    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        this.categoryService.seedCategories();
        this.userService.seedUsers();
        this.productService.seedProducts();
    }

}
