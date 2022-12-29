package com.example.football.service.impl;

import com.example.football.models.dto.PlayerSeedDto;
import com.example.football.models.dto.PlayerSeedRootDto;
import com.example.football.models.entity.Player;
import com.example.football.repository.PlayerRepository;
import com.example.football.service.PlayerService;
import com.example.football.service.StatService;
import com.example.football.service.TeamService;
import com.example.football.service.TownService;
import com.example.football.util.ValidationUtil;
import com.example.football.util.XmlParser;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.xml.bind.JAXBException;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class PlayerServiceImpl implements PlayerService {

    private static final String PLAYERS_FILE_PATH = "src/main/resources/files/xml/players.xml";
    private final PlayerRepository playerRepository;
    private final TownService townService;
    private final TeamService teamService;
    private final StatService statService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;
    private final XmlParser xmlParser;

    @Autowired
    public PlayerServiceImpl(PlayerRepository playerRepository, TownService townService, TeamService teamService,
                             StatService statService, ModelMapper modelMapper, ValidationUtil validationUtil, XmlParser xmlParser) {
        this.playerRepository = playerRepository;
        this.townService = townService;
        this.teamService = teamService;
        this.statService = statService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
        this.xmlParser = xmlParser;
    }

    @Override
    public boolean areImported() {
        return this.playerRepository.count() > 0;
    }

    @Override
    public String readPlayersFileContent() throws IOException {
        return Files.readString(Path.of(PLAYERS_FILE_PATH));
    }

    @Override
    public String importPlayers() throws JAXBException, FileNotFoundException {
        StringBuilder sb = new StringBuilder();
        PlayerSeedRootDto playerSeedRootDto = this.xmlParser.fromFile(PLAYERS_FILE_PATH, PlayerSeedRootDto.class);
//        List<Player> players =
        playerSeedRootDto.getPlayers()
                .stream()
                .filter(playerSeedDto -> {
                    boolean isValid = this.validationUtil.isValid(playerSeedDto)
                            && !isEntityExist(playerSeedDto.getEmail());
                    generateOutputContent(sb, playerSeedDto, isValid);
                    return isValid;
                })
                .map(playerSeedDto -> {
                    Player player = this.modelMapper.map(playerSeedDto, Player.class);
                    player.setTown(this.townService.findTownByName(playerSeedDto.getTown().getName()));
                    player.setTeam(this.teamService.findTownByName(playerSeedDto.getTeam().getName()));
                    player.setStat(this.statService.findStatById(playerSeedDto.getStat().getId()));
                    return player;
                })
                .forEach(playerRepository::save);
//                .toList();
        return sb.toString().trim();
    }

    private boolean isEntityExist(String email) {
        return this.playerRepository.existsByEmail(email);
    }

    private static void generateOutputContent(StringBuilder sb, PlayerSeedDto playerSeedDto, boolean isValid) {
        sb.append(isValid ? String.format("Successfully imported Player %s %s - %s"
                        , playerSeedDto.getFirstName(), playerSeedDto.getLastName(), playerSeedDto.getPosition())
                        : "Invalid Player")
                .append(System.lineSeparator());
    }

    @Override
    public String exportBestPlayers() {
        StringBuilder sb = new StringBuilder();
        LocalDate start = LocalDate.parse("01/01/1995", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        LocalDate end = LocalDate.parse("01/01/2003", DateTimeFormatter.ofPattern("dd/MM/yyyy"));
        List<Player> bestPlayers = this.playerRepository.findAllByBirthDateBetweenOrderByStat_ShootingDescStat_PassingDescStat_EnduranceDescLastName(start, end);
        bestPlayers.forEach(player ->  sb.append(String.format("Player - %s %s\n" +
                "\tPosition - %s\n" +
                "Team - %s\n" +
                "\tStadium - %s\n",
                player.getFirstName(), player.getLastName(), player.getPosition(), player.getTeam().getName(), player.getTeam().getStadiumName()    )));
        return sb.toString().trim();
    }
}