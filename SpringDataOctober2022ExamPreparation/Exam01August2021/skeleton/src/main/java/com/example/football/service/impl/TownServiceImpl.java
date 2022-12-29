package com.example.football.service.impl;

import com.example.football.models.dto.TownSeedDto;
import com.example.football.models.entity.Town;
import com.example.football.repository.TownRepository;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.google.gson.Gson;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Arrays;
import java.util.List;


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
        TownSeedDto[] townSeedDtos = this.gson.fromJson(this.readTownsFileContent(), TownSeedDto[].class);
//        List<Town> towns =
        Arrays.stream(townSeedDtos)
                .filter(townSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(townSeedDto)
                            && !isEntityExist(townSeedDto.getName());
                    generateOutputContent(sb, townSeedDto, isValid);
                    return isValid;
                })
                .map(townSeedDto -> this.modelMapper.map(townSeedDto, Town.class))
                .forEach(townRepository::save);
//                .toList();

        return sb.toString().trim();
    }

    @Override
    public Town findTownByName(String townName) {
        return this.townRepository.findByName(townName).orElse(null);
    }

    private boolean isEntityExist(String townName) {
        return this.townRepository.existsByName(townName);
    }

    private static void generateOutputContent(StringBuilder sb, TownSeedDto townSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Town %s - %d", townSeedDto.getName(), townSeedDto.getPopulation())
                        : "Invalid Town")
                .append(System.lineSeparator());
    }

}
