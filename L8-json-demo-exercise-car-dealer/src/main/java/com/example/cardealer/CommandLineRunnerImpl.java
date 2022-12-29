package com.example.cardealer;

import com.example.cardealer.model.dto.CarToyotaDto;
import com.example.cardealer.model.dto.CustomerBasicInfoDto;
import com.example.cardealer.model.dto.SupplierBasicInfoDto;
import com.example.cardealer.model.service.CarService;
import com.example.cardealer.model.service.CustomerService;
import com.example.cardealer.model.service.PartService;
import com.example.cardealer.model.service.SupplierService;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Collections;
import java.util.List;

@Component
public class CommandLineRunnerImpl implements CommandLineRunner {

    private static final String OUTPUT_PATH = "src/main/resources/files/out/";

    private static final String ORDERED_CUSTOMERS_FILE_NAME = "ordered-customers.json";
    private static final String TOYOTA_CARS_FILE_NAME = "toyota-cars.json";
    private static final String LOCAL_SUPPLIERS_FILE_NAME = "local-suppliers.json";
    private static final String CARS_AND_PARTS_FILE_NAME = "cars-and-parts.json";
    private final SupplierService supplierService;
    private final PartService partService;
    private final CarService carService;
    private final CustomerService customerService;

    private final Gson gson;

    private final BufferedReader reader;

    @Autowired
    public CommandLineRunnerImpl(SupplierService supplierService, PartService partService, CarService carService, CustomerService customerService, Gson gson) {
        this.supplierService = supplierService;
        this.partService = partService;
        this.carService = carService;
        this.customerService = customerService;
        this.gson = gson;
        this.reader = new BufferedReader(new InputStreamReader(System.in));
    }


    @Override
    public void run(String... args) throws Exception {

        seedData();

        System.out.println("Enter exercise number from 1 to 3: ");
        int exNum = Integer.parseInt(reader.readLine());

        switch (exNum) {
            case 1 -> orderedCustomers();
            case 2 -> carsFromMakeToyota();
            case 3 -> localSuppliers();
        }


    }


    private void localSuppliers() throws IOException {

        List<SupplierBasicInfoDto> supplierBasicInfoDtos = this.supplierService
                .findAllSuppliersNotFromAbroad();

        String content = this.gson.toJson(supplierBasicInfoDtos);

        writeToFile(OUTPUT_PATH + LOCAL_SUPPLIERS_FILE_NAME, content);

    }

    private void carsFromMakeToyota() throws IOException {

        List<CarToyotaDto> toyotaDtos = this.carService
                .findAllToyotasOrderByNameThenByDistanceDescending("Toyota");

        String content = this.gson.toJson(toyotaDtos);

        writeToFile(OUTPUT_PATH + TOYOTA_CARS_FILE_NAME, content);

    }

    private void orderedCustomers() throws IOException {

        List<CustomerBasicInfoDto> customerDtos = this.customerService
                .findAllCustomersOrderByBirthdate();

        String content = this.gson.toJson(customerDtos);

        writeToFile(OUTPUT_PATH + ORDERED_CUSTOMERS_FILE_NAME, content);

    }

    private void writeToFile(String filePath, String content) throws IOException {
        Files.write(Path.of(filePath), Collections.singleton(content));
    }

    private void seedData() throws IOException {
        this.supplierService.seedSuppliers();
        this.partService.seedParts();
        this.carService.seedData();
        this.customerService.seedData();
    }

}
