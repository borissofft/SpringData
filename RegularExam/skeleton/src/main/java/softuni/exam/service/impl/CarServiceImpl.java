package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CarSeedDto;
import softuni.exam.models.dto.CarSeedRootDto;
import softuni.exam.models.entity.Car;
import softuni.exam.repository.CarRepository;
import softuni.exam.service.CarService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class CarServiceImpl implements CarService {
    private static final String CARS_FILE_PATH = "src/main/resources/files/xml/cars.xml";
    private static final String INVALID_CAR = "Invalid car";
    private static final String VALID_CAR = "Successfully imported car %s - %s";
    private final CarRepository carRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public CarServiceImpl(CarRepository carRepository, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.carRepository = carRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.carRepository.count() > 0;
    }

    @Override
    public String readCarsFromFile() throws IOException {
        return Files.readString(Path.of(CARS_FILE_PATH));
    }

    @Override
    public String importCars() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        CarSeedRootDto carSeedRootDto = this.xmlParser.fromFile(CARS_FILE_PATH, CarSeedRootDto.class);
//        List<Car> cars =
        carSeedRootDto.getCars()
                .stream()
                .filter(carSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(carSeedDto)
                            && !isEntityExist(carSeedDto.getPlateNumber());
                    generateOutputContent(sb, carSeedDto, isValid);
                    return isValid;
                })
                .map(carSeedDto -> this.modelMapper.map(carSeedDto, Car.class))
                .forEach(carRepository::save);
//                        .toList();
        return sb.toString().trim();
    }

    @Override
    public boolean existCarById(long id) {
        return this.carRepository.existsById(id);
    }

    @Override
    public Car findCarById(long carId) {
        return this.carRepository.findById(carId).orElse(null);
    }

    private boolean isEntityExist(String plateNumber) {
        return this.carRepository.existsByPlateNumber(plateNumber);
    }

    private static void generateOutputContent(StringBuilder sb, CarSeedDto carSeedDto, boolean isValid) {
        sb.append(isValid ? String.format(VALID_CAR, carSeedDto.getCarMake(), carSeedDto.getCarModel())
                        : INVALID_CAR)
                .append(System.lineSeparator());
    }

}