package com.example.xmlex;

import com.example.xmlex.model.dto.*;
import com.example.xmlex.service.CategoryService;
import com.example.xmlex.service.ProductService;
import com.example.xmlex.service.UserService;
import com.example.xmlex.util.XmlParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import javax.xml.bind.JAXBException;
import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.math.BigDecimal;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String RESOURCES_FILES_PATH = "src/main/resources/files/";
    private static final String OUTPUT_FILES_PATH = "src/main/resources/files/out/";
    private static final String PRODUCTS_IN_RANGE_FILE_NAME = "products-in-range.xml";
    private static final String USERS_SOLD_PRODUCTS_FILE_NAME = "users-sold-products.xml";
    private static final String CATEGORIES_BY_PRODUCTS_FILE_NAME = "categories-by-products.xml";
    private static final String USERS_AND_PRODUCTS_FILE_NAME = "users-and-products.xml";
    private static final String CATEGORIES_FILE_NAME = "categories.xml";
    private static final String USERS_FILE_NAME = "users.xml";
    private static final String PRODUCTS_FILE_NAME = "products.xml";
    private final XmlParser xmlParser;
    private final CategoryService categoryService;
    private final UserService userService;
    private final ProductService productService;

    private final BufferedReader reader;

    @Autowired
    public CommandLineRunnerImpl(XmlParser xmlParser, CategoryService categoryService, UserService userService, ProductService productService) {
        this.xmlParser = xmlParser;
        this.categoryService = categoryService;
        this.userService = userService;
        this.productService = productService;
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

    private void usersAndProducts() throws JAXBException {

        UserViewRootTask4Dto task4Dto = this.userService.findUsersWithMoreThenOneSoldProductOrderBySoldProducts();
        System.out.println();
        this.xmlParser.writeToFile(OUTPUT_FILES_PATH + USERS_AND_PRODUCTS_FILE_NAME, task4Dto);
    }

    private void categoriesByProductsCount() throws JAXBException {

        CategoryProductSummaryDto categoryProductSummaryDto = this.productService
                .getSummaryFromCategories();

        this.xmlParser.writeToFile(OUTPUT_FILES_PATH + CATEGORIES_BY_PRODUCTS_FILE_NAME, categoryProductSummaryDto);
    }

    private void soldProducts() throws JAXBException {

        UserViewRootDto userViewRootDto = this.userService
                .findUsersWithMoreThenOneSoldProduct();

        this.xmlParser.writeToFile(OUTPUT_FILES_PATH + USERS_SOLD_PRODUCTS_FILE_NAME, userViewRootDto);

    }

    private void productsInRange() throws JAXBException {

        ProductViewRootDto rootDto = this.productService
                .findProductsInRangeWithoutBuyer();

        this.xmlParser.writeToFile(OUTPUT_FILES_PATH + PRODUCTS_IN_RANGE_FILE_NAME, rootDto);

    }

    private void seedData() throws JAXBException, FileNotFoundException {

        if (this.categoryService.getEntityCount() == 0) {

            CategorySeedRootDto categorySeedRootDto = this.xmlParser
                    .fromFile(RESOURCES_FILES_PATH + CATEGORIES_FILE_NAME, CategorySeedRootDto.class);
            this.categoryService.seedCategories(categorySeedRootDto.getCategories());

        }

        if (this.userService.getEntityCount() == 0) {

            UserSeedRootDto userSeedRootDto = this.xmlParser
                    .fromFile(RESOURCES_FILES_PATH + USERS_FILE_NAME, UserSeedRootDto.class);
            this.userService.seedUsers(userSeedRootDto.getUsers());
        }

        if (this.productService.getEntityCount() == 0) {

            ProductSeedRootDto productSeedRootDto = this.xmlParser
                    .fromFile(RESOURCES_FILES_PATH + PRODUCTS_FILE_NAME, ProductSeedRootDto.class);
            this.productService.sedProducts(productSeedRootDto.getProducts());
        }

    }

}
