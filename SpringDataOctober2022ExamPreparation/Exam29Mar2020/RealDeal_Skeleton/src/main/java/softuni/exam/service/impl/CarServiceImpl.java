package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarGeneralInfoDto;
import softuni.exam.models.dto.CarSeedDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
public class CarServiceImpl implements CarService {

    private static final String CARS_FILE_PATH = "src/main/resources/files/json/cars.json";

    private final CarRepository carRepository;
    private final Gson gson;
    private final ValidationUtil validationUtil;
    private final ModelMapper modelMapper;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, Gson gson, ValidationUtil validationUtil, ModelMapper modelMapper) {
        this.carRepository = carRepository;
        this.gson = gson;
        this.validationUtil = validationUtil;
        this.modelMapper = modelMapper;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFileContent() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException {

        StringBuilder sb = new StringBuilder();

        CarSeedDto[] carSeedDtos = this.gson.fromJson(this.readCarsFileContent(), CarSeedDto[].class);

        // Variant 1 - General
        Arrays.stream(carSeedDtos)
                .filter(carSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(carSeedDto)
                            && this.carRepository.findByMakeAndModelAndKilometers(carSeedDto.getMake(), carSeedDto.getModel(), carSeedDto.getKilometers()).isEmpty();
                    generateOutputContent(sb, carSeedDto, isValid);
                    return isValid;
                })
                .map(carSeedDto -> modelMapper.map(carSeedDto, Car.class))
                .forEach(carRepository::save);


        // Variant 2 - Only for debug and see all Car objects
//        List<Car> carList = Arrays.stream(carSeedDtos)
//                .filter(carSeedDto -> {
//                    boolean isValid = this.validationUtil.isValid(carSeedDto)
//                            && this.carRepository.findByMakeAndModelAndKilometers(carSeedDto.getMake(), carSeedDto.getModel(), carSeedDto.getKilometers()).isEmpty();
//                    generateOutputContent(sb, carSeedDto, isValid);
//                    return isValid;
//                })
//                .map(carSeedDto -> modelMapper.map(carSeedDto, Car.class))
//                .toList(); // Never try to save the list in DB, because it will enter repeating entities if exists!!! If you want to collect it firs than save use Set<> equals() and hashCode()

        // Variant 3 - Save and Flush without using filter()
//        Arrays.stream(carSeedDtos)
//                .forEach(carSeedDto -> {
//                    boolean isValid = this.validationUtil.isValid(carSeedDto)
//                            && this.carRepository.findByMakeAndModelAndKilometers(carSeedDto.getMake(), carSeedDto.getModel(), carSeedDto.getKilometers()).isEmpty();
//                    if (isValid) {
//                        this.carRepository.saveAndFlush(this.modelMapper.map(carSeedDto, Car.class));
//                    }
//                    generateOutputContent(sb, carSeedDto, isValid);
//                });

        // Variant 4 - Here the massages from valid/invalid content will be wrong if there are pictures with the same make, model and kilometers multiple times but the imported data is right,
        // because of using Set with implemented equals() and hashCode() in target Class
//        Set<Car> carSet = Arrays.stream(carSeedDtos)
//                .filter(carSeedDto -> {
//                    boolean isValid = this.validationUtil.isValid(carSeedDto);
//                    generateOutputContent(sb, carSeedDto, isValid);
//                    return isValid;
//                })
//                .map(carSeedDto -> modelMapper.map(carSeedDto, Car.class))
//                .collect(Collectors.toSet());
//        this.carRepository.saveAll(carSet);

        return sb.toString().trim();
    }

    private static void generateOutputContent(StringBuilder sb, CarSeedDto carSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported car - %s - %s"
                        , carSeedDto.getMake(), carSeedDto.getModel())
                        : "Invalid car")
                .append(System.lineSeparator());
    }

    @Override
    public String getCarsOrderByPicturesCountThenByMake() {

        List<Car> cars = this.carRepository.findAllCarsOrderByPicturesCountDescThanByMake();

        List<CarGeneralInfoDto> carGeneralInfoDtos = cars
                .stream()
                .map(car -> {
                    CarGeneralInfoDto carGeneralInfoDto = modelMapper.map(car, CarGeneralInfoDto.class);
                    carGeneralInfoDto.setPicturesCount(car.getPictures().size());
                    return carGeneralInfoDto;
                })
                .toList();

        StringBuilder sb = new StringBuilder();
        carGeneralInfoDtos.forEach(carGeneralInfoDto -> sb.append(carGeneralInfoDto.toString()));

        return sb.toString().trim();
    }

    @Override
    public Car findById(Long id) {
        return this.carRepository
                .findById(id)
                .orElse(null);
    }

}
