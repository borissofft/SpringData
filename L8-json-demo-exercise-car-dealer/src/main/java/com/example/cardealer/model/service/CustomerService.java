package com.example.cardealer.model.service;

import com.example.cardealer.model.dto.CustomerBasicInfoDto;

import java.io.IOException;
import java.util.List;

public interface CustomerService {
    void seedData() throws IOException;

    List<CustomerBasicInfoDto> findAllCustomersOrderByBirthdate();
}
