package softuni.exam.service.impl;

import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import softuni.exam.models.dto.TownsSeedDto;
import softuni.exam.models.entity.Town;
import softuni.exam.repository.TownRepository;
import softuni.exam.service.TownService;
import softuni.exam.util.ValidationUtil;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;

@Service
public class TownServiceImpl implements TownService {
    private static final String TOWNS_FILE_PATH = "src/main/resources/files/json/towns.json";

    private final TownRepository townRepository;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public TownServiceImpl(TownRepository townRepository, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.townRepository = townRepository;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.townRepository.count() > 0;
    }

    @Override
    public String readTownsFileContent() throws IOException {
        return Files.readString(Path.of(TOWNS_FILE_PATH));
    }

    @Override
    public String importTowns() throws IOException {
        StringBuilder sb = new StringBuilder();
        TownsSeedDto[] townsSeedDtos = this.gson.fromJson(this.readTownsFileContent(), TownsSeedDto[].class);

        Arrays.stream(townsSeedDtos)
                .filter(townsSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(townsSeedDto)
                            && !isEntityExist(townsSeedDto.getTownName());
                    generateOutputContent(sb, townsSeedDto, isValid);
                    return isValid;
                })
                .map(townsSeedDto -> this.modelMapper.map(townsSeedDto, Town.class))
                .forEach(townRepository::save);

        return sb.toString().trim();
    }

    @Override
    public Town findTownByName(String townName) {
        return this.townRepository.findByTownName(townName).orElse(null);
    }

    private boolean isEntityExist(String townName) {
        return this.townRepository.existsByTownName(townName);
    }

    private static void generateOutputContent(StringBuilder sb, TownsSeedDto townsSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported town %s - %d", townsSeedDto.getTownName(), townsSeedDto.getPopulation())
                        : "Invalid town")
                .append(System.lineSeparator());
    }

}
