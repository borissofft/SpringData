package com.example.cardealer.model.service.impl;

import com.example.cardealer.model.dto.CarSeedDto;
import com.example.cardealer.model.dto.CarToyotaDto;
import com.example.cardealer.model.entity.Car;
import com.example.cardealer.model.entity.Part;
import com.example.cardealer.model.repository.CarRepository;
import com.example.cardealer.model.service.CarService;
import com.example.cardealer.model.service.PartService;
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
import java.util.Set;
import java.util.stream.Collectors;

import static com.example.cardealer.constants.GlobalConstants.RESOURCES_FILE_PATH;

@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_NAME = "cars.json";
    private final CarRepository carRepository;
    private final Gson gson;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    private final PartService partService;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ModelMapper modelMapper, ValidationUtil validationUtil, PartService partService) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.partService = partService;
    }

    @Override
    public void seedData() throws IOException {

        if (this.carRepository.count() > 0) {
            return;
        }

//        TypeMap<CarSeedDto, Car> carTypeMap = modelMapper.createTypeMap(CarSeedDto.class, Car.class)
//                .addMappings(mapping -> {
//                    mapping.skip();
//                });

        String fileContent = Files.readString(Path.of(RESOURCES_FILE_PATH + CARS_FILE_NAME));

        CarSeedDto[] carSeedDtos = this.gson.fromJson(fileContent, CarSeedDto[].class);

        List<Car> carList = Arrays.stream(carSeedDtos)
                .filter(validationUtil::isValid)
                .map(carSeedDto -> {
                    Car car = modelMapper.map(carSeedDto, Car.class);
                    Set<Part> parts = this.partService.findRandomParts();
//                    parts.forEach(part -> part.setSupplier(null));
                    car.setParts(parts);
                    return car;
                }).collect(Collectors.toList());
        carRepository.saveAll(carList);

    }

    @Override
    public List<CarToyotaDto> findAllToyotasOrderByNameThenByDistanceDescending(String toyota) {
        return this.carRepository.findAllToyota(toyota)
                .stream()
                .map(car -> modelMapper.map(car, CarToyotaDto.class)
                )
                .collect(Collectors.toList());
    }


}
