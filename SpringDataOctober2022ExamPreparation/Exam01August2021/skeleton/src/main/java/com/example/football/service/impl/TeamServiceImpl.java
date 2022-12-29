package com.example.football.service.impl;

import com.example.football.models.dto.TeamSeedDto;
import com.example.football.models.entity.Team;
import com.example.football.models.entity.Town;
import com.example.football.repository.TeamRepository;
import com.example.football.service.TeamService;
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
public class TeamServiceImpl implements TeamService {

    private static final String TEAMS_FILE_PATH = "src/main/resources/files/json/teams.json";
    private final TeamRepository teamRepository;
    private final TownService townService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final Gson gson;

    @Autowired
    public TeamServiceImpl(TeamRepository teamRepository, TownService townService, ModelMapper modelMapper, ValidationUtil validationUtil, Gson gson) {
        this.teamRepository = teamRepository;
        this.townService = townService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.gson = gson;
    }

    @Override
    public boolean areImported() {
        return this.teamRepository.count() > 0;
    }

    @Override
    public String readTeamsFileContent() throws IOException {
        return Files.readString(Path.of(TEAMS_FILE_PATH));
    }

    @Override
    public String importTeams() throws IOException {
        StringBuilder sb = new StringBuilder();
        TeamSeedDto[] teamSeedDtos = this.gson.fromJson(this.readTeamsFileContent(), TeamSeedDto[].class);
//        List<Team> teams =
        Arrays.stream(teamSeedDtos)
                .filter(teamSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(teamSeedDto)
                            && !isEntityExist(teamSeedDto.getName());
                    generateOutputContent(sb, teamSeedDto, isValid);
                    return isValid;
                })
                .map(teamSeedDto -> {
                    Team team = this.modelMapper.map(teamSeedDto, Team.class);
                    Town town = this.townService.findTownByName(teamSeedDto.getTownName());
                    team.setTown(town);
                    return team;
                })
                .forEach(this.teamRepository::save);
//                        .toList();

        return sb.toString().trim();
    }

    @Override
    public Team findTownByName(String teamName) {
        return this.teamRepository.findByName(teamName).orElse(null);
    }

    private boolean isEntityExist(String teamName) {
        return this.teamRepository.existsByName(teamName);
    }

    private static void generateOutputContent(StringBuilder sb, TeamSeedDto teamSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Team %s - %d", teamSeedDto.getName(), teamSeedDto.getFanBase())
                        : "Invalid Team")
                .append(System.lineSeparator());
    }

}