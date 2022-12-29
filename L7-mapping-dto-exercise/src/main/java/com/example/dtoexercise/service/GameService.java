package com.example.dtoexercise.service;


import com.example.dtoexercise.model.dto.GameAddDto;
import com.example.dtoexercise.model.entity.Game;

import java.math.BigDecimal;

public interface GameService {
    void addGame(GameAddDto gameAddDto);

    void editGame(Long gameId, BigDecimal price, Double size);

    void deleteGame(long gameId);

    void printTitlesAndPriceForAllGames();

    void getDetailsFromGame(String title);

}
