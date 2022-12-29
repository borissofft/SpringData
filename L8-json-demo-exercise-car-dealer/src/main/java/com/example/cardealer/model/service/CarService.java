package com.example.cardealer.model.service;

import com.example.cardealer.model.dto.CarToyotaDto;

import java.io.IOException;
import java.util.List;

public interface CarService {
    void seedData() throws IOException;

    List<CarToyotaDto> findAllToyotasOrderByNameThenByDistanceDescending(String toyota);

}
