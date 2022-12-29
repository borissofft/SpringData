package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.CountrySeedDto;
import softuni.exam.models.entity.Country;
import softuni.exam.repository.CountryRepository;
import softuni.exam.service.CountryService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class CountryServiceImpl implements CountryService {

    private static final String COUNTRIES_FILE_PATH = "src/main/resources/files/json/countries.json";
    private final CountryRepository countryRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public CountryServiceImpl(CountryRepository countryRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.countryRepository = countryRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.countryRepository.count() > 0;
    }

    @Override
    public String readCountriesFromFile() throws IOException {
        return Files.readString(Path.of(COUNTRIES_FILE_PATH));
    }

    @Override
    public String importCountries() throws IOException {
        StringBuilder sb = new StringBuilder();

        CountrySeedDto[] countrySeedDtos = this.gson.fromJson(readCountriesFromFile(), CountrySeedDto[].class);
        Arrays.stream(countrySeedDtos)
                .filter(countrySeedDto -> {
                    boolean isValid = this.validationUtil.isValid(countrySeedDto)
                            && !isEntityExist(countrySeedDto.getCountryName());
                    generateOutputContent(sb, countrySeedDto, isValid);
                    return isValid;
                })
                .map(countrySeedDto -> this.modelMapper.map(countrySeedDto, Country.class))
                .forEach(countryRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Country findCountryById(long country) {
        return this.countryRepository.findById(country).orElse(null);
    }

    private boolean isEntityExist(String countryName) {
        return this.countryRepository.existsByCountryName(countryName);
    }

    private static void generateOutputContent(StringBuilder sb, CountrySeedDto countrySeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported country %s - %s", countrySeedDto.getCountryName(), countrySeedDto.getCurrency())
                        : "Invalid country")
                .append(System.lineSeparator());
    }

}
