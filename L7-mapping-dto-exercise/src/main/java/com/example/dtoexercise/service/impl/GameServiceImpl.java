package com.example.dtoexercise.service.impl;

import com.example.dtoexercise.model.dto.GameAddDto;
import com.example.dtoexercise.model.dto.GameViewDetails;
import com.example.dtoexercise.model.dto.GameViewTitlePriceDto;
import com.example.dtoexercise.model.entity.Game;
import com.example.dtoexercise.repository.GameRepository;
import com.example.dtoexercise.service.GameService;
import com.example.dtoexercise.service.UserService;
import com.example.dtoexercise.util.ValidationUtil;
import org.modelmapper.ModelMapper;
import org.modelmapper.TypeMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.validation.ConstraintViolation;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;

@Service
public class GameServiceImpl implements GameService {

    private final GameRepository gameRepository;
    private final UserService userService;
    private final ModelMapper modelMapper;
    private final ValidationUtil validationUtil;

    @Autowired
    public GameServiceImpl(GameRepository gameRepository, UserService userService, ModelMapper modelMapper, ValidationUtil validationUtil) {
        this.gameRepository = gameRepository;
        this.userService = userService;
        this.modelMapper = modelMapper;
        this.validationUtil = validationUtil;
    }

    @Override
    public void addGame(GameAddDto gameAddDto) {

        Game toCheck = this.gameRepository.findFirstByTitle(gameAddDto.getTitle());

        if (toCheck != null) {
            System.out.printf("Game with name: %s already exist! Enter other game.%n", gameAddDto.getTitle());
            return;
        }

        Set<ConstraintViolation<GameAddDto>> violations = this.validationUtil.getViolations(gameAddDto);

        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
            return;
        }

        Game game = this.modelMapper.map(gameAddDto, Game.class);

        // TODO save in DB
        this.gameRepository.save(game);
        System.out.printf("Added %s%n", game.getTitle());

    }

    @Override
    public void editGame(Long gameId, BigDecimal price, Double size) {

        Game game = this.gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.printf("Game with id: %s don't exist %n", gameId);
            return;
        }

        game.setPrice(price);
        game.setSize(size);
        this.gameRepository.save(game);
        System.out.printf("Edited %s%n", game.getTitle());

    }

    @Override
    public void deleteGame(long gameId) {

        Game game = this.gameRepository.findById(gameId).orElse(null);

        if (game == null) {
            System.out.printf("Game with id: %s don't exist %n", gameId);
            return;
        }


        this.gameRepository.delete(game);
        System.out.printf("Deleted %s%n", game.getTitle());

    }

    @Override
    public void printTitlesAndPriceForAllGames() {

        List<Game> allGames = this.gameRepository.findAll();

        if (allGames.size() == 0) {
            System.out.println("No saved games, no info!");
            return;
        }

        TypeMap<Game, GameViewTitlePriceDto> gameGameViewTitlePriceDtoTypeMap = this.modelMapper.createTypeMap(Game.class, GameViewTitlePriceDto.class)
                .addMappings(src -> {
                    src.map(Game::getTitle, GameViewTitlePriceDto::setTitle);
                    src.map(Game::getPrice, GameViewTitlePriceDto::setPrice);
                });

        List<GameViewTitlePriceDto> gamesToPrint = allGames.stream().map(gameGameViewTitlePriceDtoTypeMap::map).toList();

        Set<ConstraintViolation<List<GameViewTitlePriceDto>>> violations = this.validationUtil.getViolations(gamesToPrint);

        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
            return;
        }

        allGames.forEach(game -> System.out.printf("%s %.2f%n", game.getTitle(), game.getPrice()));
    }

    @Override
    public void getDetailsFromGame(String title) {

        Game game = this.gameRepository.findFirstByTitle(title);

        if (game == null) {
            System.out.printf("No game with name: %s, no details!%n", title);
            return;
        }


        GameViewDetails details = this.modelMapper.map(game, GameViewDetails.class);

        Set<ConstraintViolation<GameViewDetails>> violations = this.validationUtil.getViolations(details);

        if (!violations.isEmpty()) {
            violations.stream().map(ConstraintViolation::getMessage).forEach(System.out::println);
            return;
        }

        System.out.printf("Title: %s%n" +
                "Price: %.2f %n" +
                "Description: %s. %n" +
                "Release date: %s%n",
                details.getTitle(), details.getPrice(), details.getDescription(), details.getReleaseDate());
    }

}
