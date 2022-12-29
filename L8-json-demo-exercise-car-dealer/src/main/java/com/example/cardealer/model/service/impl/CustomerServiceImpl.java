package com.example.cardealer.model.service.impl;

import com.example.cardealer.model.dto.CustomerBasicInfoDto;
import com.example.cardealer.model.dto.CustomerSeedDto;
import com.example.cardealer.model.entity.Customer;
import com.example.cardealer.model.repository.CustomerRepository;
import com.example.cardealer.model.service.CustomerService;
import com.example.cardealer.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.example.cardealer.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class CustomerServiceImpl implements CustomerService {

    private static final String CUSTOMERS_FILE_NAME = "customers.json";
    private final CustomerRepository customerRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public CustomerServiceImpl(CustomerRepository customerRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.customerRepository = customerRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void seedData() throws IOException {

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CUSTOMERS_FILE_NAME));

        CustomerSeedDto[] customerSeedDtos = this.gson.fromJson(fileContent, CustomerSeedDto[].class);

        Arrays.stream(customerSeedDtos)
                .filter(validationUtil::isValid)
                .map(customerSeedDto -> modelMapper.map(customerSeedDto, Customer.class))
                .forEach(customerRepository::save);

    }

    @Override
    public List<CustomerBasicInfoDto> findAllCustomersOrderByBirthdate() {

        return  this.customerRepository.findAll()
                .stream()
                .sorted((o1, o2) -> o1.getBirthDate().compareTo(o2.getBirthDate()))
                .map(customer -> {
                    CustomerBasicInfoDto customerBasicInfoDto = modelMapper
                            .map(customer, CustomerBasicInfoDto.class);
                    return customerBasicInfoDto;
                }).collect(Collectors.toList());

    }

}
