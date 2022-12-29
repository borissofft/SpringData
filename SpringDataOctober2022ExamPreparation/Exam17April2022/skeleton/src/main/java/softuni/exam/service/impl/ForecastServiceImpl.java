package softuni.exam.service.impl;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.ForecastSeedDto;
import softuni.exam.models.dto.ForecastSeedRootDto;
import softuni.exam.models.entity.City;
import softuni.exam.models.entity.DayOfWeek;
import softuni.exam.models.entity.Forecast;
import softuni.exam.repository.ForecastRepository;
import softuni.exam.service.CityService;
import softuni.exam.service.ForecastService;
import softuni.exam.util.ValidationUtil;
import softuni.exam.util.XmlParser;

import javax.xml.bind.JAXBException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;

@Service
public class ForecastServiceImpl implements ForecastService {

    private static final String FORECASTS_FILE_PATH = "src/main/resources/files/xml/forecasts.xml";
    private static final String EXPORT_FORECAST = "City: %s:\n" +
            "-min temperature: %.2f\n" +
            "--max temperature: %.2f\n" +
            "---sunrise: %s\n" +
            "----sunset: %s\n";
    private final ForecastRepository forecastRepository;
    private final CityService cityService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public ForecastServiceImpl(ForecastRepository forecastRepository, CityService cityService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.forecastRepository = forecastRepository;
        this.cityService = cityService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.forecastRepository.count() > 0;
    }

    @Override
    public String readForecastsFromFile() throws IOException {
        return Files.readString(Path.of(FORECASTS_FILE_PATH));
    }

    @Override
    public String importForecasts() throws IOException, JAXBException {
        StringBuilder sb = new StringBuilder();
        ForecastSeedRootDto forecastSeedRootDto = this.xmlParser.fromFile(FORECASTS_FILE_PATH, ForecastSeedRootDto.class);

//        List<Forecast> forecastList =
        forecastSeedRootDto.getForecasts()
                .stream()
                .filter(forecastSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(forecastSeedDto)
                            && !isForecastByDayAndCityExist(forecastSeedDto.getDayOfWeek(), forecastSeedDto.getCity());
                    generateOutputContent(sb, forecastSeedDto, isValid);
                    return isValid;
                })
                .map(forecastSeedDto -> {
                    Forecast forecast = this.modelMapper.map(forecastSeedDto, Forecast.class);
                    City city = this.cityService.getCityById(forecastSeedDto.getCity());
                    forecast.setCity(city);
                    return forecast;
                })
                .forEach(forecastRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    private boolean isForecastByDayAndCityExist(DayOfWeek dayOfWeek, long cityId) {
        City city = this.cityService.getCityById(cityId);
        return this.forecastRepository.existsByDayOfWeekAndCity(dayOfWeek, city);
    }

    private static void generateOutputContent(StringBuilder sb, ForecastSeedDto forecastSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully import forecast %s - %.2f", forecastSeedDto.getDayOfWeek(), forecastSeedDto.getMaxTemperature())
                        : "Invalid forecast")
                .append(System.lineSeparator());
    }

    @Override
    public String exportForecasts() {
        StringBuilder sb = new StringBuilder();
        List<Forecast> forecasts = this.forecastRepository.findAllByDayOfWeekAndCity_PopulationLessThanOrderByMaxTemperatureDescIdAsc(DayOfWeek.SUNDAY, 150000);
        forecasts.forEach(forecast ->
                sb.append(String.format(EXPORT_FORECAST,
                        forecast.getCity().getCityName(), forecast.getMinTemperature(), forecast.getMaxTemperature(), forecast.getSunrise(), forecast.getSunset())));
        return sb.toString().trim();
    }
}
