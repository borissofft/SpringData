package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CitySeedDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CityRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;

@Service
public class CityServiceImpl implements CityService {

    private static final String CITIES_FILE_PATH = "src/main/resources/files/json/cities.json";

    private final CityRepository cityRepository;
    private final CountryService countryService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CityServiceImpl(CityRepository cityRepository, CountryService countryService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.cityRepository = cityRepository;
        this.countryService = countryService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.cityRepository.count() > 0;
    }

    @Override
    public String readCitiesFileContent() throws IOException {
        return Files.readString(Path.of(CITIES_FILE_PATH));
    }

    @Override
    public String importCities() throws IOException {
        StringBuilder sb = new StringBuilder();
        CitySeedDto[] citySeedDtos = this.gson.fromJson(this.readCitiesFileContent(), CitySeedDto[].class);
//        List<City> cities =
        Arrays.stream(citySeedDtos)
                .filter(citySeedDto -> {
                    boolean isValid = this.validationUtil.isValid(citySeedDto)
                            && !isEntityExist(citySeedDto.getCityName());
                    generateOutputContent(sb, citySeedDto, isValid);
                    return isValid;
                })
                .map(citySeedDto -> {
                    City city = this.modelMapper.map(citySeedDto, City.class);
                    Country country = this.countryService.findCountryById(citySeedDto.getCountry());
                    city.setCountry(country);
                    return city;
                })
                .forEach(cityRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    @Override
    public City getCityById(long cityId) {
        return this.cityRepository.findById(cityId).orElse(null);
    }

    private boolean isEntityExist(String cityName) {
        return this.cityRepository.existsByCityName(cityName);
    }

    private static void generateOutputContent(StringBuilder sb, CitySeedDto citySeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported city %s - %d", citySeedDto.getCityName(), citySeedDto.getPopulation())
                        : "Invalid city")
                .append(System.lineSeparator());
    }

}
